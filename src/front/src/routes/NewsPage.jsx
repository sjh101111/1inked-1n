import React, { useEffect, useState } from 'react';
import axios from 'axios';
import News from '../components/News';
import Header from '../components/Layout/Header'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import { Button } from "@/components/ui/button"

function NewsPage() {
    const [newsItems, setNewsItems] = useState([]);
    const [newsQuery, setNewsQuery] = useState(`시사, 경제 ,IT`)
    const [newsDisplay, setNewsDisplay] = useState(20);
    const [newsStart, setNewsStart] = useState(1);
    const [newsSort, setNewsSort] = useState(`date`)



    const handleSortChange = (event) => {
        setNewsStart(1);
        setNewsSort(event.target.value);
        fetchNews();
    };

    const handleQueryChange = (event) => {
        setNewsStart(1);
        setNewsQuery(event.target.value);
        fetchNews();
    };
    //API에서 뉴스 가져오는거
    useEffect(() => {
        fetchNews();
    }, []);

    const seeMore = () => {
        setNewsStart(newsStart += 20);
        fetchNews(true);
    }

    const fetchNews = async (append = false) => {
        try {
            const params = {
                sort: newsSort,
                display: newsDisplay,
                start: newsStart,
                query: newsQuery
            };

            const response = await axios.get('URL_TO_YOUR_NEWS_API', { params });

            const newItems = response.data.item;
            if (append) {
                setNewsItems(prev => [...prev, ...newItems]);
            } else {
                setNewsItems(newItems);
            }
        } catch (error) {
            console.error('Error fetching news:', error);
        }
    };

    return (
        <div>
            <Header />
            <div className="flex">
                <Select onChange={handleSortChange}>
                    <SelectTrigger className="w-[180px]">
                        <SelectValue placeholder="정렬" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="date">날짜순</SelectItem>
                        <SelectItem value="sim">정확도순</SelectItem>
                    </SelectContent>
                </Select>

                <Select onChange={handleQueryChange}>
                    <SelectTrigger className="w-[180px]">
                        <SelectValue placeholder="키워드" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="시사">시사</SelectItem>
                        <SelectItem value="경제">경제</SelectItem>
                        <SelectItem value="IT">IT</SelectItem>
                    </SelectContent>
                </Select>
                <Button onClick={seeMore} className="bg-[#6866EB] w-48 hover:bg-violet-600">
                    조건에 맞는 기사 찾기
                </Button>
            </div>

            <div className="flex flex-col items-center">


                <article>
                    {newsItems ? (
                        newsItems.map((item, index) => (
                            <News
                                key={index}
                                title={item.title}
                                description={item.description}
                                originallink={item.originallink}
                                pubDate={item.pubDate}
                            />
                        ))
                    ) : (
                        <p>뉴스 정보가 없습니다.</p>
                    )}
                </article>

                <Button onClick={seeMore} className="bg-[#6866EB] w-48 hover:bg-violet-600">
                    See More
                </Button>
            </div>
        </div>
    );
}
export default NewsPage