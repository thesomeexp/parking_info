package com.someexp.parking_info.util;

import java.util.List;

import org.springframework.data.domain.Page;

public class Page4Navigator<T> {
    Page<T> pageFromJPA;
    int navigatePages;

    int totalPages;

    int number;

    long totalElements;

    int size;

    int numberOfElements;

    List<T> content;

    boolean isHasContent;

    boolean first;

    boolean last;

    boolean isHasNext;

    boolean isHasPrevious;

    int[] navigatepageNums;

    public Page4Navigator() {
        //这个空的分页是为了 Redis 从 json格式转换为 Page4Navigator 对象而专门提供的
    }

    public Page4Navigator(Page<T> pageFromJPA,int navigatePages) throws Exception{
        this.pageFromJPA = pageFromJPA;
        this.navigatePages = navigatePages;

        totalPages = pageFromJPA.getTotalPages();

        number  = pageFromJPA.getNumber() ;

        totalElements = pageFromJPA.getTotalElements();

        size = pageFromJPA.getSize();

        numberOfElements = pageFromJPA.getNumberOfElements();

        content = pageFromJPA.getContent();

        isHasContent = pageFromJPA.hasContent();

        first = pageFromJPA.isFirst();

        last = pageFromJPA.isLast();

        isHasNext = pageFromJPA.hasNext();

        isHasPrevious  = pageFromJPA.hasPrevious();

        calcNavigatepageNums();

    }

    private void calcNavigatepageNums()  throws Exception{
        int navigatepageNums[];
        int totalPages = getTotalPages();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
        this.navigatepageNums = navigatepageNums;
    }

    public int getNavigatePages() throws Exception {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) throws Exception {
        this.navigatePages = navigatePages;
    }

    public int getTotalPages() throws Exception {
        return totalPages;
    }

    public void setTotalPages(int totalPages) throws Exception {
        this.totalPages = totalPages;
    }

    public int getNumber() throws Exception {
        return number;
    }

    public void setNumber(int number) throws Exception {
        this.number = number;
    }

    public long getTotalElements() throws Exception {
        return totalElements;
    }

    public void setTotalElements(long totalElements) throws Exception {
        this.totalElements = totalElements;
    }

    public int getSize() throws Exception {
        return size;
    }

    public void setSize(int size) throws Exception {
        this.size = size;
    }

    public int getNumberOfElements() throws Exception {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) throws Exception {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() throws Exception {
        return content;
    }

    public void setContent(List<T> content) throws Exception {
        this.content = content;
    }

    public boolean isHasContent() throws Exception {
        return isHasContent;
    }

    public void setHasContent(boolean isHasContent) throws Exception {
        this.isHasContent = isHasContent;
    }

    public boolean isFirst() throws Exception {
        return first;
    }

    public void setFirst(boolean first) throws Exception {
        this.first = first;
    }

    public boolean isLast() throws Exception {
        return last;
    }

    public void setLast(boolean last) throws Exception {
        this.last = last;
    }

    public boolean isHasNext() throws Exception {
        return isHasNext;
    }

    public void setHasNext(boolean isHasNext) throws Exception {
        this.isHasNext = isHasNext;
    }

    public boolean isHasPrevious() throws Exception {
        return isHasPrevious;
    }

    public void setHasPrevious(boolean isHasPrevious) throws Exception {
        this.isHasPrevious = isHasPrevious;
    }

    public int[] getNavigatepageNums() throws Exception {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) throws Exception {
        this.navigatepageNums = navigatepageNums;
    }

}