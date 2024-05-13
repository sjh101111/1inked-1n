import Article from "@/components/Article";
import Header from "@/components/Layout/Header";

const MainFeed = () =>{
    return (
        <main className="h-screen">
            <Header className="fixed w-full"></Header>
            <section className="article-feed pt-[61px] px-[20%]">
                <div className="py-4 overflow-auto grid gap-4 justify-center ">
                    <Article></Article>
                    <Article></Article>
                    <Article></Article>
                    <Article></Article>
                    <Article></Article>
                </div>
            </section>
        </main>
    );
}

export default MainFeed;