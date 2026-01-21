package com.twilight.twilight.domain.book.repository.book;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.book.dto.GetBookInfoDto;
import com.twilight.twilight.domain.book.entity.book.Book;
import com.twilight.twilight.domain.book.entity.book.QBook;
import com.twilight.twilight.domain.book.entity.book.QBookTags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookQueryRepositoryImpl implements BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetBookInfoDto> findBooksByTagsLimit(List<Long> tagIds, int limit) {

        if (tagIds == null || tagIds.isEmpty()) return List.of();
        if (tagIds.size() > 4) throw new IllegalArgumentException("최대 4개 태그만 지원합니다.");

        QBook book = QBook.book;
        QBookTags bt1 = new QBookTags("bt1"); // 기준(희귀 태그)
        QBookTags bt2 = new QBookTags("bt2");
        QBookTags bt3 = new QBookTags("bt3");
        QBookTags bt4 = new QBookTags("bt4");

        JPAQuery<GetBookInfoDto> query = queryFactory
                .select(Projections.constructor(
                        GetBookInfoDto.class,
                        book.bookId,
                        book.name,
                        book.author,
                        book.pageCount,
                        book.description
                ))
                .from(bt1)
                .join(bt1.book, book)
                .where(bt1.tag.tagId.eq(tagIds.get(0))); // 첫 태그

        if (tagIds.size() >= 2) {
            query.join(bt2).on(
                    bt2.book.eq(book),
                    bt2.tag.tagId.eq(tagIds.get(1))
            );
        }

        if (tagIds.size() >= 3) {
            query.join(bt3).on(
                    bt3.book.eq(book),
                    bt3.tag.tagId.eq(tagIds.get(2))
            );
        }

        if (tagIds.size() >= 4) {
            query.join(bt4).on(
                    bt4.book.eq(book),
                    bt4.tag.tagId.eq(tagIds.get(3))
            );
        }

        return query
                .distinct()
                .limit(limit)
                .fetch();
    }
}
