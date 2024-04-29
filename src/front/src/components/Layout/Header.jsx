import { Link } from "react-router-dom";
import SearchIcon from "../svg/SearchIcon";
import { Input } from "../ui/input";

const Header = () =>{
    return (
        <header className="flex items-center justify-between px-4 py-2 border-b dark:border-gray-800">
            <div className="flex items-center gap-2">
                <Link to="/">
                    <h2>Logo</h2>
                </Link>
                <div className="flex items-center rounded-sm border gap-4">
                    <SearchIcon></SearchIcon>
                    <Input type="text" placeholder="원하는 키워드를 입력해보세요!"></Input>
                </div>
            </div>

            <div className="flex items-center gap-4">

            </div>
        </header>
    );
};

export default Header;