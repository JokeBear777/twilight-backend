package com.twilight.twilight.domain.book.service;

import com.twilight.twilight.domain.book.dto.*;
import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.book.entity.book.BookTags;
import com.twilight.twilight.domain.book.entity.question.MemberQuestion;
import com.twilight.twilight.domain.book.entity.question.MemberQuestionAnswer;
import com.twilight.twilight.domain.book.entity.recommendation.Recommendation;
import com.twilight.twilight.domain.book.entity.tag.Tag;
import com.twilight.twilight.domain.book.infra.stats.TagStatsRepository;
import com.twilight.twilight.domain.book.repository.book.BookQueryRepository;
import com.twilight.twilight.domain.book.repository.book.BookRepository;
import com.twilight.twilight.domain.book.repository.question.AnswerTagMappingRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionAnswerRepository;
import com.twilight.twilight.domain.book.repository.question.MemberQuestionRepository;
import com.twilight.twilight.domain.book.repository.recommendation.RecommendationRepository;
import com.twilight.twilight.domain.book.repository.tag.BookTagsRepository;
import com.twilight.twilight.domain.book.repository.tag.TagRepository;
import com.twilight.twilight.domain.member.entity.*;
import com.twilight.twilight.domain.member.repository.MemberInterestRepository;
import com.twilight.twilight.domain.member.repository.MemberPersonalityRepository;
import com.twilight.twilight.domain.member.repository.MemberRepository;
import com.twilight.twilight.domain.member.repository.PersonalityRepository;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.cache.MemberQuestionCache;
import com.twilight.twilight.global.gateway.ai.AiGateway;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private static final int MIN = 5;
    private static final int MAX = 15;
    private static final int RANDOM_PICK = 10;
    private static final int TAG_ANSWER_COUNT = 5;

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberQuestionAnswerRepository memberQuestionAnswerRepository;
    private final BookRepository bookRepository;
    private final MemberQuestionCache memberQuestionCache;
    private final AnswerTagMappingRepository answerTagMappingRepository;
    private final MemberRepository memberRepository;
    private final PersonalityRepository personalityRepository;
    private final MemberPersonalityRepository memberPersonalityRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final AiGateway aiGateway;
    private final RecommendationRepository recommendationRepository;
    private final BookTagsRepository bookTagsRepository;
    private final TagRepository tagRepository;
    private final BookQueryRepository bookQueryRepository;
    private final TagStatsRepository tagStatsRepository;

    public QuestionAnswerResponseDto createCategoryQuestionAndAnswer() {
        MemberQuestion categoryQuestion =
                memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.CATEGORY.name())
                        .orElseThrow(() -> new RuntimeException("CATEGORY 질문이 없습니다"));

        List<QuestionAnswerResponseDto.AnswerDto> answers = memberQuestionAnswerRepository.findByMemberQuestion(categoryQuestion)
                .stream()
                .map(a -> new QuestionAnswerResponseDto.AnswerDto(a.getMemberQuestionAnswerId(), a.getAnswer(),
                        answerTagMappingRepository.findTagIdByAnswerId(a.getMemberQuestionAnswerId())))
                .toList();

        return QuestionAnswerResponseDto.builder()
                .question(categoryQuestion.getQuestion())
                .answers(answers)
                .questionType(categoryQuestion.getQuestionType().name())
                .build();
    }

    public List<QuestionAnswerResponseDto> createRandomQuestionAndAnswerVer2(Long tagId) {
        Set<Long> selectedIds = new HashSet<>();
        List<MemberQuestion> questions = new ArrayList<>();

        while (questions.size() < 3) {
            MemberQuestion emotionQuestion = memberQuestionRepository
                    .findRandomByQuestionTypeAndCategory(MemberQuestion.questionType.EMOTION.name(), tagId)
                    .orElseThrow(() -> new RuntimeException("EMOTION 질문이 없습니다"));

            if (!selectedIds.contains(emotionQuestion.getMemberQuestionId())) {
                selectedIds.add(emotionQuestion.getMemberQuestionId());
                questions.add(emotionQuestion);
            }
        }


        MemberQuestion themeQuestion = memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.NATURAL.name())
                .orElseThrow(() -> new RuntimeException("NATURAL 질문이 없습니다"));

        questions.add(themeQuestion);

        log.info("Emotion + Natural 질문 {}개 생성", questions.size());

        return questions.stream().map(q -> {
            List<QuestionAnswerResponseDto.AnswerDto> answers = memberQuestionAnswerRepository.findByMemberQuestion(q)
                    .stream()
                    .map(a -> new QuestionAnswerResponseDto.AnswerDto(a.getMemberQuestionAnswerId(), a.getAnswer(), null))
                    .collect(Collectors.toList());
            //return new QuestionAnswerResponseDto(q.getQuestion(), answers,q.getQuestionType().name());
            return QuestionAnswerResponseDto.builder()
                    .question(q.getQuestion())
                    .answers(answers)
                    .questionType(q.getQuestionType().name())
                    .build();
        }).collect(Collectors.toList());

    }

    /**
    public List<QuestionAnswerResponseDto> createRandomQuestionAndAnswer() {

        Set<Long> selectedIds = new HashSet<>();
        List<MemberQuestion> questions = new ArrayList<>();

        MemberQuestion categoryQuestion =
                memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.CATEGORY.name())
                        .orElseThrow(() -> new RuntimeException("CATEGORY 질문이 없습니다"));

        questions.add(categoryQuestion);
            /////////////////////대분류 먼저 받고 그거 바탕으로 감성 질문 받자. 그냥 폼으로 받고 url로바꾸자 그게 맞음
        while (questions.size() < 4) {
            MemberQuestion emotionQuestion = memberQuestionRepository
                    .findRandomByQuestionTypeAndCategory(MemberQuestion.questionType.EMOTION.name(), categoryQuestion.getQuestionType().name())
                    .orElseThrow(() -> new RuntimeException("EMOTION 질문이 없습니다"));

            if (!selectedIds.contains(emotionQuestion.getMemberQuestionId())) {
                selectedIds.add(emotionQuestion.getMemberQuestionId());
                questions.add(emotionQuestion);
            }
        }


        MemberQuestion themeQuestion = memberQuestionRepository.findRandomByQuestionType(MemberQuestion.questionType.NATURAL.name())
                .orElseThrow(() -> new RuntimeException("NATURAL 질문이 없습니다"));

        questions.add(themeQuestion);


        return questions.stream().map(q -> {
            List<QuestionAnswerResponseDto.AnswerDto> answers = memberQuestionAnswerRepository.findByMemberQuestion(q)
                    .stream()
                    .map(a -> new QuestionAnswerResponseDto.AnswerDto(a.getMemberQuestionAnswerId(), a.getAnswer()))
                    .collect(Collectors.toList());
            //return new QuestionAnswerResponseDto(q.getQuestion(), answers,q.getQuestionType().name());
            return QuestionAnswerResponseDto.builder()
                    .question(q.getQuestion())
                    .answers(answers)
                    .questionType(q.getQuestionType().name())
                    .build();
        }).collect(Collectors.toList());
    }
     **/

    private List<Tag> getTagList(BookRecommendationRequestDto request) {
        List<Tag> tagList = new ArrayList<>();

        //대질문 태그 먼저 넣기
        tagList.add(tagRepository.findById(request.getTagId())
                .orElseThrow(()->new RuntimeException("사용자 답변에 해당하는 대분류를 찾을 수 없습니다.")));

        for (int i = 0; i < request.getAnswerIds().size(); i++) {
            Long answerId = request.getAnswerIds().get(i);
            // -1이면 자연어 질문 -> 태그 매핑 건너뜀
            if (answerId == -1L) continue;
            tagList.add(findTagByAnswerId(answerId));
        }

        log.info("tagList count ={}", tagList.size());

        return tagList;
    }

    @Transactional(readOnly = true)
    public void requestRecommendationVer2(
            BookRecommendationRequestDto request,
            CustomUserDetails userDetails
    ) {
        List<Tag> tagList = getTagList(request);
        List<Long> sortedTagIds = getSortedTagIdList(tagList);
        List<GetBookInfoDto> one = pickByOneTag(sortedTagIds);
        if (sendIfDecided(one, userDetails, request, tagList)) {
            return;
        }

        List<GetBookInfoDto> two = pickByTwoTags(sortedTagIds);
        if (sendIfDecided(two, userDetails, request, tagList)) {
            return;
        }

        List<GetBookInfoDto> three = pickByThreeTags(sortedTagIds);
        if (sendIfDecided(three, userDetails, request, tagList)) {
            return;
        }

        List<GetBookInfoDto> four = pickByFourTags(sortedTagIds);
        if (sendIfDecided(four, userDetails, request, tagList)) {
            return;
        }

        //다안되면 랜덤으로
        List<GetBookInfoDto> randomBookList = selectRandomBookList(four);
        sendInfoToAiServer(randomBookList, userDetails.getMember(), request, tagList);

    }

    private boolean sendIfDecided(
            List<GetBookInfoDto> candidates,
            CustomUserDetails userDetails,
            BookRecommendationRequestDto request,
            List<Tag> tagList
    ) {
        if (candidates.isEmpty()) {
            throw new NoSuchElementException("조건에 맞는 책이 없습니다.");
        }

        if (isTooSmall(candidates) || isGoodRange(candidates)) {
            sendInfoToAiServer(trimToMax15(candidates),
                    userDetails.getMember(),
                    request,
                    tagList);
            return true;
        }

        return false; //아직 더 좁혀야 함 (tooLarge)
    }

    private List<GetBookInfoDto> pickByFourTags(List<Long> sortedTagIds) {

        if (sortedTagIds == null || sortedTagIds.size() < 4) {
            throw new IllegalArgumentException("4개 태그 조합을 위해 tagId가 4개 필요합니다.");
        }

        return bookQueryRepository.findBooksByTagsLimit(
                List.of(
                        sortedTagIds.get(0),
                        sortedTagIds.get(1),
                        sortedTagIds.get(2),
                        sortedTagIds.get(3)
                ),
                16
        );
    }

    private List<GetBookInfoDto> pickByOneTag(List<Long> sortedTagIds) {
        for (Long baseTagId : sortedTagIds) {

            List<GetBookInfoDto> candidates =
                    bookQueryRepository.findBooksByTagsLimit(List.of(baseTagId), 16);

            // 너무 작으면 다음(덜 희귀한) 태그로 넘어감
            if (isTooSmall(candidates)) continue;

            // 적당히 크면 바로 반환
            return candidates;
        }

        // 여기까지 왔다는 건 "모든 태그가 너무 작다"는 뜻
        // => 그나마 가장 큰 태그(= 가장 덜 희귀한 tag)를 반환
        Long lastTagId = sortedTagIds.get(sortedTagIds.size() - 1);

        return bookQueryRepository.findBooksByTagsLimit(List.of(lastTagId), 16);
    }

    private List<GetBookInfoDto> pickByTwoTags(List<Long> sortedTagIds) {
        if (sortedTagIds.size() < 2) {
            throw new IllegalArgumentException("2개 태그 조합을 위해 tagId가 2개 이상 필요합니다.");
        }

        Long base = sortedTagIds.get(0);

        List<Long> partners;

        if (sortedTagIds.size() == 2) {
            partners = List.of(sortedTagIds.get(1));
        } else if (sortedTagIds.size() == 3) {
            partners = List.of(sortedTagIds.get(1), sortedTagIds.get(2));
        } else {
            partners = List.of(
                    sortedTagIds.get(1),
                    sortedTagIds.get(2),
                    sortedTagIds.get(3)
            );
        }

        for (Long partner : partners) {

            List<Long> two = List.of(base, partner);

            List<GetBookInfoDto> candidates =
                    bookQueryRepository.findBooksByTagsLimit(two, 16);

            // 아무것도 없으면 다음 조합
            if (candidates.isEmpty()) continue;

            // 너무 작으면(=필터링이 과했다) 다음 조합
            if (isTooSmall(candidates)) continue;

            // goodRange면 바로 확정
            if (isGoodRange(candidates)) return candidates;

            // tooLarge라도 일단 “one보다 줄어든 결과”니까 일단 반환하고
            // 상위에서 3태그로 더 좁히면 됨
            return candidates;
        }

        // 너무 작아지는 것만 피했으니, 마지막 조합 결과라도 반환
        return bookQueryRepository.findBooksByTagsLimit(
                List.of(base, sortedTagIds.get(sortedTagIds.size() - 1)),
                16
        );
    }

    private List<GetBookInfoDto> pickByThreeTags(List<Long> sortedTagIds) {

        if (sortedTagIds == null || sortedTagIds.size() < 3) {
            throw new IllegalArgumentException("3개 태그 조합을 위해 tagId가 3개 이상 필요합니다.");
        }

        Long t0 = sortedTagIds.get(0);
        Long t1 = sortedTagIds.get(1);

        List<Long> base = List.of(t0, t1);

        for (int i = 2; i < sortedTagIds.size(); i++) {

            Long extra = sortedTagIds.get(i);

            List<Long> three = List.of(t0, t1, extra);

            List<GetBookInfoDto> candidates =
                    bookQueryRepository.findBooksByTagsLimit(three, 16);

            if (candidates.isEmpty()) continue;

            if (isTooSmall(candidates)) continue;

            if (isGoodRange(candidates)) return candidates;

            return candidates;
        }

        return bookQueryRepository.findBooksByTagsLimit(
                List.of(t0, t1, sortedTagIds.get(2)),
                16
        );
    }

    private List<Long> getSortedTagIdList(List<Tag> tagList) {
        List<Long> tagIdList = getTagIds(tagList);
        return tagStatsRepository.getSortedTagIdList(tagIdList);
    }

    private boolean isGoodRange(List<GetBookInfoDto> books) {
        return books.size() >= 5 && books.size() <= 15;
    }

    private boolean isTooBig(List<GetBookInfoDto> books) {
        return books.size() > 15 ;
    }

    private boolean isTooSmall(List<GetBookInfoDto> books) {
        return books.size() < 5;
    }

    private List<GetBookInfoDto> trimToMax15(List<GetBookInfoDto> books) {
        if (books.size() <= 15) return books;
        return books.subList(0, 15);
    }

    private List<Long> getTagIds(List<Tag> tagList) {
        return tagList.stream()
                .map(Tag::getTagId)
                .toList();
    }

    /*
    //태그 찾아서 검색 시스템 개발하자
    @Transactional(readOnly=true)
    public void requestRecommendation(
            BookRecommendationRequestDto request,
            CustomUserDetails userDetails
            ) {

        List<Tag> tagList = getTagList(request);

        //일정개수 이하로 추려지면 사용자 정보와 함께 ai서버로 보낸다, 남은 책의 개수가 5~15개 사이가 될때까지 필터링을 한다
        //대분류
        log.info("[test] 대분류");
        int byOneTagCount = bookRepository.countBooksByTagId(tagList.get(0).getTagId());
        if (byOneTagCount == 0) {
            throw new NoSuchElementException("대분류: " + tagList.get(0).getTagId() + "에 맞는 책이 없습니다");
        }
        if (byOneTagCount < 5 ) { //대분류만으로 분류가 끝나면(도서 5개 미만), 바로 서버로 보냄
            List<Book> bookListByCategory = bookRepository.findBooksByTagId(tagList.get(0).getTagId());
            sendInfoToAiServer(bookListByCategory, userDetails.getMember(), request, tagList);
            return;
        }



        //대 + 감성 A
        log.info("[test] 대 + 감성A");
        int byTwoTagsCount = bookRepository.countBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
        if (5 <= byTwoTagsCount  && byTwoTagsCount <= 15) {
            List<Book> bookListByA = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
            sendInfoToAiServer(bookListByA, userDetails.getMember(), request, tagList);
            return;
        }
        if (byTwoTagsCount < 5 ) {
            // 대+감성A
            List<Book> booksA = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());

            // 대+감성B
            List<Book> booksB = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(2).getTagId());
            Set<Book> combinedBooks = new HashSet<>();
            combinedBooks.addAll(booksA);
            combinedBooks.addAll(booksB);

            if (combinedBooks.size() < 5) {
                // 대+감성C
                List<Book> booksC = bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(3).getTagId());
                combinedBooks.addAll(booksC);
            }

            // 랜덤으로 보내거나 전체 다 보냄
            List<Book> finalBookList = selectRandomBookList(new ArrayList<>(combinedBooks));
            sendInfoToAiServer(finalBookList, userDetails.getMember(), request, tagList);
            return;
        }

        //대 + 감성 A B
        log.info("[test] 대 + 감성A B");
        int byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByAB = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
            sendInfoToAiServer(bookListByAB, userDetails.getMember(), request, tagList);
            return;
        }
        if (byThreeTagsCount < 5 ) {
            List<Book> bookListByA =
                    bookRepository.findBooksByTwoTags(tagList.get(0).getTagId(), tagList.get(1).getTagId());
            sendInfoToAiServer(selectRandomBookList(bookListByA), userDetails.getMember(), request, tagList);
            return;
        }

        //대 + 감성 A C
        log.info("[test] 대 + 감성A C");
        byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByAC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByAC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byThreeTagsCount < 5 ) {
            List<Book> bookListByAB = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(2).getTagId());
            sendInfoToAiServer(
                    selectRandomBookList(bookListByAB), userDetails.getMember(), request, tagList);
            return;
        }

        //대 + 감성 B C
        log.info("[test] 대 + 감성B C");
        byThreeTagsCount = bookRepository.countBooksByThreeTags(
                tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
        if (5 <= byThreeTagsCount  && byThreeTagsCount <= 15) {
            List<Book> bookListByBC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByBC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byThreeTagsCount < 5 ) {
            List<Book> bookListByAC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(
                    selectRandomBookList(bookListByAC), userDetails.getMember(), request, tagList);
            return;
        }


        //대 + 감성 ABC
        log.info("[test] 대 + 감성A B C");
        int byFourTagsCount = bookRepository.countBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        if (5 <= byFourTagsCount  && byFourTagsCount <= 15) {
            List<Book> bookListByABC = bookRepository.findBooksByFourTags(
                    tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                    tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(bookListByABC, userDetails.getMember(), request, tagList);
            return;
        }
        if(byFourTagsCount < 5 ) {
            List<Book> bookListByBC = bookRepository.findBooksByThreeTags(
                    tagList.get(0).getTagId(), tagList.get(2).getTagId(), tagList.get(3).getTagId());
            sendInfoToAiServer(selectRandomBookList(
                    selectRandomBookList(bookListByBC)), userDetails.getMember(), request, tagList);
            return;
        }


        //이래도 분류가 안되면 4개 분류 + 랜덤
        log.info("[test] 대 + 감성A B C + 랜덤");
        Set<Integer> randomIndexSet = new HashSet<>();
        List<Book> bookList = bookRepository.findBooksByFourTags(
                tagList.get(0).getTagId(), tagList.get(1).getTagId(),
                tagList.get(2).getTagId(), tagList.get(3).getTagId());
        List<Book> randomBookList = selectRandomBookList(bookList);
        sendInfoToAiServer(randomBookList, userDetails.getMember(), request, tagList);
    }
    */


    private List<GetBookInfoDto> selectRandomBookList(List<GetBookInfoDto> bookList) {
        int pickSize = Math.min(10, bookList.size());

        Set<Integer> randomIndexSet = new HashSet<>();
        while (randomIndexSet.size() < pickSize) {
            int randomIndex = ThreadLocalRandom.current().nextInt(bookList.size());
            randomIndexSet.add(randomIndex);
        }

        List<GetBookInfoDto> randomBookList = new ArrayList<>();
        for (Integer index : randomIndexSet) {
            randomBookList.add(bookList.get(index));
        }

        return randomBookList;
    }

    private Tag findTagByAnswerId(Long answerId) {
        if (answerId == null || answerId == -1L) {
            log.info("자연어 질문이거나 id = -1 → 태그 없음, answerId={}", answerId);
            return null;
        }

        Tag tag = answerTagMappingRepository.findByMemberQuestionAnswer_MemberQuestionAnswerId(answerId)
                .orElseThrow(() -> new NoSuchElementException("해당 답변에 매핑된 태그가 없습니다."))
                .getTag();

        log.info("태그 결과: id = {}, name = {}", tag.getTagId(), tag.getName());

        return tag;
    }

    private void sendInfoToAiServer(
            List<GetBookInfoDto> bookList,
            Member member,
            BookRecommendationRequestDto request,
            List<Tag> tagList) {

        AiRecommendationPayload.MemberInfo memberInfo = mapMemberToPayload(member, request, tagList);
        List<AiRecommendationPayload.BooksInfo> booksInfoList = mapBookListToPayload(bookList);
        AiRecommendationPayload aiRecommendationPayload = AiRecommendationPayload.builder()
                .memberInfo(memberInfo)
                .bookInfo(booksInfoList)
                .build();

        log.info("추천 대상 도서 제목 리스트: {}",
                bookList.stream()
                        .map(GetBookInfoDto::getName)
                        .collect(Collectors.toList())
        );

        aiGateway.send(aiRecommendationPayload);
    }

    private AiRecommendationPayload.MemberInfo mapMemberToPayload (
            Member member,
            BookRecommendationRequestDto request,
            List<Tag> tagList
            ) {
        List<String> personalityStringList =
                memberPersonalityRepository.findByMember_memberId(member.getMemberId())
                        .stream()
                        .map(MemberPersonality::getPersonality)
                        .filter(Objects::nonNull)                     // getPersonality() null 방어
                        .map(Personality::getName)
                        .filter(Objects::nonNull)                     // getName() null 방어
                        .toList();

        List<String> interestStringList =
                memberInterestRepository.findByMember_memberId(member.getMemberId())
                        .stream()
                        .map(MemberInterests::getInterest)
                        .filter(Objects::nonNull)                     // getPersonality() null 방어
                        .map(Interest::getName)
                        .filter(Objects::nonNull)                     // getName() null 방어
                        .toList();

        List<AiRecommendationPayload.QuestionAnswer> questionAnswersList = new ArrayList<>();

        for (int i = 0; i < request.getQuestions().size(); i++) {

            String matchingTag = (i < tagList.size()) ? tagList.get(i).getName()
                    : null;   // step5는 null

            questionAnswersList.add(
                    AiRecommendationPayload.QuestionAnswer.builder()
                            .question(request.getQuestions().get(i))
                            .userAnswer(request.getAnswerTexts().get(i))
                            .matchingTag(matchingTag)
                            .build()
            );
        }

        return AiRecommendationPayload.MemberInfo.builder()
                .memberId(member.getMemberId())
                .age(member.getAge())
                .gender(member.getGender())
                .personalities(personalityStringList)
                .interests(interestStringList)
                .questionAnswers(questionAnswersList)
                .build();
    }

    private List<AiRecommendationPayload.BooksInfo> mapBookListToPayload(List<GetBookInfoDto> bookList) {
        log.info("👉 받은 Book 개수: {}", bookList.size());
        bookList.forEach(book -> log.info("Book name: {}", book.getName()));

        return  bookList.stream()
                .map(
                        (book) -> AiRecommendationPayload.BooksInfo.builder()
                                .bookId(book.getBookId())
                                .name(book.getName())
                                .author(book.getAuthor())
                                .pageCount(book.getPageCount())
                                .description(book.getDescription())
                                .build()
                )
                .toList();
    }

    public void completeRecommendation(
            CompleteRecommendationDto completeRecommendationDto,
            String token) {
        if (! token.equals(System.getenv("AI_SECRET_TOKEN"))) {
            log.info("식별되지 않은 ai 서버 접근");
            return;
        }
        //추천 결과 중복 방지하기 위해 지움
        recommendationRepository.deleteByMemberId(completeRecommendationDto.getMemberId());

        recommendationRepository.save(
                Recommendation.builder()
                        .memberId(completeRecommendationDto.getMemberId())
                        .bookId(completeRecommendationDto.getBookId())
                        .aiAnswer(completeRecommendationDto.getAiAnswer())
                        .build()
        );
    }

    public Optional<Recommendation> findRecommendationOnly(Long memberId) {
        return recommendationRepository.findByMemberId(memberId);
    }

    public void deleteRecommendation(Long memberId) {
        recommendationRepository.findByMemberId(memberId)
                .ifPresent(recommendationRepository::delete);
    }

    @Transactional(readOnly = true)
    public Book getBookInfo(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(()-> new RuntimeException("해당하는 책이 없습니다."));
    }

    @Transactional(readOnly = true)
    public String getBookCategory(Long bookId) {
        BookTags bookTag = bookTagsRepository.getBookCategoryByBookBookId(bookId)
                .orElseThrow(() ->new RuntimeException("책의 카테고리를 찾을 수 없습니다."));

        return bookTag.getTag().getName();
    }

    @Transactional(readOnly = true)
    public RecommendationViewDto getRecommendationViewDto(
            Long bookId,
            String aiAnswer
    ){
        Book bookResult =
                getBookInfo(bookId);
        String bookCategory = getBookCategory(bookId);

        return RecommendationViewDto.builder()
                .aiAnswer(aiAnswer)
                .bookId(bookResult.getBookId())
                .bookCover(bookResult.getCoverImageUrl())
                .title(bookResult.getName())
                .author(bookResult.getAuthor())
                .publisher(bookResult.getPublisher())
                .pubData(bookResult.getPublishedAt())
                .genre(bookCategory)
                .build();
    }


}
