export function createInfiniteScroll({
                                         root,
                                         listEl,
                                         fetchFn,
                                         renderItem,
                                         emptyMessage = "데이터가 없습니다."
                                     }) {
    let isLoading = false;
    let hasNext = true;
    let cursor = null;
    let emptyRendered = false;

    async function load() {
        if (isLoading || !hasNext) return;
        isLoading = true;

        try {
            const res = await fetchFn(cursor);
            const data = Array.isArray(res.data) ? res.data : [];

            // ✅ empty 처리 (첫 페이지)
            if (data.length === 0 && cursor === null && !emptyRendered) {
                const p = document.createElement("p");
                p.textContent = emptyMessage;
                p.style.textAlign = "center";
                p.style.color = "#777";
                p.style.margin = "2rem 0";
                listEl.appendChild(p);
                emptyRendered = true;
                hasNext = false;
                return;
            }

            data.forEach(item => {
                listEl.appendChild(renderItem(item));
            });

            hasNext = res.hasNext === true;
            cursor = res.nextCursor ?? null;

        } catch (e) {
            console.error("[InfiniteScroll] error", e);
            hasNext = false;
        } finally {
            isLoading = false;
        }
    }

    function onScroll() {
        const { scrollTop, clientHeight, scrollHeight } = root;
        if (scrollTop + clientHeight >= scrollHeight - 200) {
            load();
        }
    }

    root.addEventListener("scroll", onScroll);

    // 초기 로딩
    load();

    return {
        reload() {
            listEl.innerHTML = "";
            cursor = null;
            hasNext = true;
            emptyRendered = false;
            load();
        }
    };
}