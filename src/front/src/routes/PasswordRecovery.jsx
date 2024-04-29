import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import LabelSection from "@/components/Layout/LabelSection";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"

const PasswordRecovery = () => {
    //라우팅 네비게이터
    const nevigate = useNavigate();
    
    const mainpage = () =>{
        setLogin(true);
        nevigate("/login");
    }


    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="flex flex-col items-center w-500 py-20 px-10 mt-60 bg-white border">
                <h1>1INKED 1N</h1>
                <h3>비밀번호 찾기</h3>
                <LabelSection asChild label="Email">
                    <Input id="tBox_email" type="email" placeholder="Email Address"></Input>
                </LabelSection>

                <LabelSection asChild label="Password recovery Question" className="mt-4">
                    <Select>
                        <SelectTrigger>
                            <SelectValue placeholder="Question List" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="elementry-school">내가 태어난 초등학교는</SelectItem>
                            <SelectItem value="child-nickname">내 어릴적 별명은?</SelectItem>
                            <SelectItem value="first-love">내 첫사랑 이름은?</SelectItem>
                        </SelectContent>
                    </Select>

                    <Input id="tBox_pwd" type="input" placeholder="your answer"></Input>

                </LabelSection>

                {/* 버튼은 onClick 콜백 동작 불가 */}
                <Button asChild className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">
                    <div onClick={mainpage}>비밀번호 찾기</div>
                </Button>
            </section>
        </main>
    );
};

export default PasswordRecovery;