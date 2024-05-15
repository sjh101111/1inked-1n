import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import ArticleDialog from "@/components/ArticleDialog";
import { logout } from "@/utils/API";
import {  useState } from "react";
import { useUserInfo } from "@/utils/store";
import {searchUsers} from "@/utils/API";
import {Command, CommandInput,CommandList} from "@/components/ui/command"

const Header = (props) =>{
    const { userInfo } = useUserInfo();
    const [searchResults, setSearchResults] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [throttle, setThrottle] = useState(false);

    const fetchSearchUsers = (keyword) => {
        if(!throttle){
            setThrottle(true);
            setTimeout(() =>{
                searchUsers(keyword)
                .then(response => {
                    setSearchResults(response); // 검색 결과를 상태에 저장
                })
                .catch(error => {
                    console.error('Error:', error);
                });
                setThrottle(false);
            }, 200);
        }
    };

    const handleSearchSubmit = (ev) => {
        const keyCode = ev.code == "Enter" ? "submit" : "keydown";
        if(keyCode == "Enter"){
            if (searchTerm.length > 2) { // 키워드 길이가 3 이상일 때 검색 실행
                fetchSearchUsers(searchTerm);
            } else {
                alert('키워드는 두 글자 이상이어야 합니다.');
                setSearchResults([]); // 검색어 길이가 짧을 경우 결과를 비움
                setShowResults(false);
            }
        }else{
           if (searchTerm.length > 2){
              fetchSearchUsers(searchTerm);
           }
        }
        
    };

    return (
        <header className={`flex items-center border-b bg-white dark:border-gray-800 overflow-hidden z-10 ${props.className}`}>
            <div className="flex items-center px-4 py-2 justify-between overflow-hidden w-full">
                <div className="flex items-center gap-2 min-w-[600px]">
                    <Link to="/">
                        <h2>Logo</h2>
                    </Link>
                    <div className="flex items-center overflow-hidden w-[400px]">
                        <Command onKeyDown={handleSearchSubmit} className="rounded-lg border w-full">
                            <CommandInput placeholder="원하는 프로필을 찾아보세요!" onValueChange={setSearchTerm}></CommandInput>
                            <CommandList className={`fixed top-14 w-[400px] z-10 bg-white rounded-lg ${searchResults.length > 0 ? "border" : "" }`}>
                                    {
                                        searchResults.map(user => (
                                                    <Link to="/userPage" state={{email: user.email}} key={user.id} className="flex gap-4 p-2 hover:bg-gray-100">
                                                            <Avatar className="w-10 h-10">
                                                                <AvatarImage alt="유저 프로필" src={`data:image/png;base64,${user.image}`} />
                                                                <AvatarFallback />
                                                            </Avatar>
                                                            <div className="flex flex-col">
                                                                <span className="font-medium text-base">{user.realName}</span>
                                                                <span className="text-sm text-black/50">{user.email}</span>
                                                            </div>
                                                    </Link>
                                                    ))
                                    }
                            </CommandList>
                        </Command>
                    </div>
                </div>

                <div className="flex items-center gap-4 min-w-[350px]">
                    <Link to="/newsinfo">뉴스</Link>
                    <Link to="/resume">Resume</Link>
                    <Link to="/chat">쪽지</Link>
                    <Link to="/mypage" className="flex items-center ml-2 gap-2">
                        <Avatar className="w-10 h-10">
                            <AvatarImage alt="유저 프로필" src={`${userInfo?.profileSrc}`} />
                            <AvatarFallback></AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col justify-center">
                            <h2 className="font-medium text-base">{userInfo?.realName}</h2>
                            <span className="text-sm text-black/50">{userInfo?.email}</span>
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