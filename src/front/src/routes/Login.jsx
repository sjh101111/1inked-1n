import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import LabelSection from "@/components/Layout/LabelSection";

const Login = () =>{
    //라우팅 네비게이터
    const nevigate = useNavigate();

    //글로벌 컨텍스트
    const {isLogin, setLogin} = useContext(GlobalContext);

    const doLogin = () =>{
        setLogin(true);
        nevigate("/");
    }

    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="w-500 py-20 px-10 mt-60 bg-white border">
                <LabelSection asChild label="Email">
                    <Input id="tBox_email" type="email" placeholder="Email Address"></Input>
                </LabelSection>
                <LabelSection asChild label="Password" className="mt-4">
                    <Input id="tBox_pwd" type="password" placeholder="password"></Input>
                </LabelSection>
                {/* 버튼은 onClick 콜백 동작 불가 */}
                <Button asChild className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">
                    <div onClick={doLogin}>로그인</div>
                </Button>
                <div className="flex justify-center mt-4">
                    <Link to="/findPassword" className="text-black/65">비밀번호를 잊으셨나요?</Link>
                </div>
            </section>
        </main>
    );
}

export default Login;