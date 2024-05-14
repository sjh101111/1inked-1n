import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button"
import SearchIcon from "../svg/SearchIcon";
import { Input } from "../ui/input";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import ArticleDialog from "@/components/ArticleDialog";
import { logout } from "@/utils/API";

const Header = (props) =>{
    return (
        <header className={`flex items-center border-b bg-white dark:border-gray-800 overflow-hidden z-10 ${props.className}`}>
            <div className="flex items-center px-4 py-2 justify-between overflow-hidden w-full">
                <div className="flex items-center gap-2 min-w-[500px]">
                    <Link to="/">
                        <h2>Logo</h2>
                    </Link>
                    <div className="flex items-center rounded-sm border gap-2 pl-2 w-full overflow-hidden">
                        <SearchIcon></SearchIcon>
                        <Input className="flex-grow focus-visible:ring-transparent border-0" type="text" placeholder="원하는 키워드를 입력해보세요!"></Input>
                    </div>
                </div>

                <div className="flex items-center gap-4 min-w-[350px]">
                    <Link to="/newsinfo">뉴스</Link>
                    <Link to="/resumeAi">Resume</Link>
                    <Link to="/chat">쪽지</Link>
                    <Link to="/mypage" className="flex items-center ml-2 gap-2">
                        <Avatar className="w-10 h-10">
                            <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                            <AvatarFallback>hi</AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col justify-center">
                            <h2 className="font-medium text-base">username</h2>
                            <span className="text-sm text-black/50 ">id</span>
                        </div>
                    </Link>
                </div>
            </div>
            <div className="flex px-4 gap-4 items-center justify-center border-l w-2/12">
                <ArticleDialog>
                    <Button className="bg-[#6866EB] hover:bg-violet-600">Create</Button>
                </ArticleDialog>
                <Button onClick={logout} className="bg-[#6866EB] hover:bg-violet-600">Logout</Button>
            </div>
        </header>
    );
};

export default Header;