import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button"
import SearchIcon from "../svg/SearchIcon";
import { Input } from "../ui/input";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import ArticleDialog from "@/components/ArticleDialog";
import { fetchLoginUserProfile, logout } from "@/utils/API";
import { useEffect, useState } from "react";
import { useUserInfo } from "@/utils/store";
import {searchUsers} from "@/utils/API";

const Header = (props) =>{
    const { userInfo } = useUserInfo();
    const [searchResults, setSearchResults] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [showResults, setShowResults] = useState(false)

    useEffect(() => {
        console.log(userInfo)
    }, [userInfo]);

    const fetchSearchUsers = (keyword) => {
        searchUsers(keyword)
            .then(response => {
                console.log(response);
                setSearchResults(response); // 검색 결과를 상태에 저장
                setShowResults(true); // 검색 결과 표시
            })
            .catch(error => {
                console.error('Error:', error);
                setShowResults(false); // 에러 발생 시 결과 숨김
            });
    };

    const handleSearchChange = (event) => {
        const { value } = event.target;
        setSearchTerm(value);
    };

    const handleSearchSubmit = () => {
        if (searchTerm.length > 2) { // 키워드 길이가 3 이상일 때 검색 실행
            fetchSearchUsers(searchTerm);
        } else {
            alert('키워드는 두 글자 이상이어야 합니다.');
            setSearchResults([]); // 검색어 길이가 짧을 경우 결과를 비움
            setShowResults(false);
        }
    };

    const toggleResultsDisplay = () => {
        setShowResults(!showResults);
    };

    return (
        <header className={`flex items-center border-b bg-white dark:border-gray-800 overflow-hidden z-10 ${props.className}`}>
            <div className="flex items-center px-4 py-2 justify-between overflow-hidden w-full">
                <div className="flex items-center gap-2 min-w-[500px]">
                    <Link to="/">
                        <h2>Logo</h2>
                    </Link>
                    <div className="flex items-center rounded-sm border gap-2 pl-2 w-full overflow-hidden">
                        <button onClick={handleSearchSubmit}>
                            <SearchIcon></SearchIcon>
                        </button>
                            <Input className="flex-grow focus-visible:ring-transparent border-0" type="text"
                                   placeholder="원하는 키워드를 입력해보세요!" value={searchTerm}
                                   onChange={handleSearchChange}></Input>
                    </div>
                </div>

                <div className="flex items-center gap-4 min-w-[350px]">
                    <Link to="/newsinfo">뉴스</Link>
                    <Link to="/resume">Resume</Link>
                    <Link to="/chat">쪽지</Link>
                    <Link to="/mypage" className="flex items-center ml-2 gap-2">
                        <Avatar className="w-10 h-10">
                            <AvatarImage alt="유저 프로필" src={`data:image/png;base64,${userInfo?.profileSrc}`} />
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
            {showResults && (
                <div className="absolute bg-white shadow-lg mt-12 p-4 w-full max-w-md">
                    {searchResults.length > 0 ? (
                        searchResults.map(user => (
                            <Link to="/userPage" state={{ email: user.email }} key={user.id} className="block p-2 hover:bg-gray-100">
                                <div className="flex items-center gap-2">
                                    <Avatar className="w-10 h-10">
                                        <AvatarImage alt="유저 프로필" src={`data:image/png;base64,${user.image}`} />
                                        <AvatarFallback />
                                    </Avatar>
                                    <div className="flex flex-col">
                                        <span className="font-medium text-base">{user.realName}</span>
                                        <span className="text-sm text-black/50">{user.email}</span>
                                    </div>
                                </div>
                            </Link>
                        ))
                    ) : (
                        <div className="text-center text-sm text-gray-500">검색 결과가 없습니다.</div>
                    )}
                </div>
            )}
        </header>
    );
};

export default Header;