import React, { useEffect, useState } from 'react';
import News from '../components/News';
import Header from "@/components/Layout/Header.jsx";
import { fetchNewsItems } from "../utils/API"
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { removeHtmlLabels } from '@/utils/common';

function NewsPage() {
    const [newsItems, setNewsItems] = useState([]);
    const [newsQuery, setNewsQuery] = useState(`IT`)
    const [newsPage, setNewsPage] = useState(1);
    const [newsSort, setNewsSort] = useState(`date`)

    const handleSortChange = (event) => {
        setNewsPage(1);
        setNewsSort(event);
        fetchNews();
    };

    const handleQueryChange = (event) => {
        setNewsPage(1);
        setNewsQuery(event);
       fetchNews();
    };
    //API에서 뉴스 가져오는거
    useEffect(() => {
        fetchNews();
    }, []);

    const seeMore = () => {
        setNewsPage(newsPage + 1);
        fetchNews(true);
    }

    const serchNews =() =>{
        setNewsItems([]);
        fetchNews();
    }

    const fetchNews = async (append = false) => {
        try {
            const params = {
                query: newsQuery,
                page: newsPage,
                sort: newsSort,
            };

            const response = await fetchNewsItems(params);

            if (append) {
                //중복 제거 작업
                const mapper = {};
                newsItems.forEach(item =>{
                   mapper[item.link]  = item;
                });

                const filteredResponses = [...response].filter(item =>{
                    return !mapper[item.link];
                });

                setNewsItems(prev => [...(prev || []), ...filteredResponses]);
            } else {
                setNewsItems(response);
            }
        } catch (error) {
            console.error('Error fetching news:', error);
        }
    };

    return (
        <>
            <Header />
            <div className="p-4">
                <div className="flex gap-2">
                    <Select onValueChange={handleSortChange}>
                        <SelectTrigger className="w-[180px]">
                            <SelectValue placeholder="정렬" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="date">날짜순</SelectItem>
                            <SelectItem value="sim">정확도순</SelectItem>
                        </SelectContent>
                    </Select>

                    <Select onValueChange={handleQueryChange}>
                        <SelectTrigger className="w-[180px]">
                            <SelectValue placeholder="키워드" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="시사">시사</SelectItem>
                            <SelectItem value="경제">경제</SelectItem>
                            <SelectItem value="IT">IT</SelectItem>
                        </SelectContent>
                    </Select>
                    <Button onClick={serchNews} className="bg-[#6866EB] w-48 hover:bg-violet-600">
                        조건에 맞는 기사 찾기
                    </Button>
                </div>

                <section className="mt-4 flex flex-col items-center gap-4">
                    {newsItems.length > 0 ? (
                        newsItems.map((item, index) => (
                            <News
                                key={index}
                                title={removeHtmlLabels(item.title)}
                                description={removeHtmlLabels(item.description)}
                                link={item.link}
                            />
                        ))
                    ) : (
                        <p>뉴스 정보가 없습니다.</p>
                    )}
                </section>

                <div className="w-full flex justify-center mt-4">
                    <Button onClick={seeMore} className="bg-[#6866EB] w-48 hover:bg-violet-600">
                        See More
                    </Button>
                </div>
            </div>
        </>
    );
}
export default NewsPage