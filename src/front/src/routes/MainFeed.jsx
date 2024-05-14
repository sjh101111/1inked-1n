import Article from "@/components/Article";
import Header from "@/components/Layout/Header";
import { readMainFeedArticles } from "@/utils/API";
import { getArticleItems } from "@/utils/Items";
import { useEffect, useState } from "react";

const MainFeed = () =>{
    const [articles, setArticles] = useState([]);

    useEffect(() =>{
        readMainFeedArticles()
        .then((articles) =>{
            setArticles(articles);
        });
    }, []);

    const afterArticleDelete = (articleId) =>{
        setArticles(articles.filter(item => item.id !== articleId));
    }

    return (
        <main className="h-screen">
            <Header className="fixed w-full"></Header>
            <section className="article-feed pt-[61px] px-[20%]">
                <div className="py-4 overflow-auto grid gap-4 justify-center ">
                   {
                    getArticleItems(articles, afterArticleDelete)
                   } 
                </div>
            </section>
        </main>
    );
}

export default MainFeed;