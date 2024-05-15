import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import LabelSection from "@/components/Layout/LabelSection";
import { correctRegxEmail, correctRegxPwd } from "@/utils/common";
import { loginReqParam } from "@/utils/Parameter";
import { fetchLoginUserProfile, login } from "@/utils/API";
import { useLogin, useUserInfo } from "@/utils/store";

const Login = () =>{
    //라우팅 네비게이터
    const nevigate = useNavigate();

    //글로벌 컨텍스트
    const {isLogin, setLogin} = useLogin();
    const {userInfo, setUserInfo} = useUserInfo();

    //state
    const [email, setEmail] = useState("");
    const [pwd, setPwd ] = useState("");

    useEffect(() =>{
        if(isLogin){
            nevigate("/");
        }
    });

    const doLogin = async () =>{
        if(!correctRegxEmail(email)){
            alert("이메일 형식이 올바르지 않습니다.");
            return ;
        }

        if(!correctRegxPwd(pwd)){
            alert("비밀번호 형식이 올바르지 않습니다.");
            return ;
        }

        const reqParam = loginReqParam(email, pwd);
        
        login(reqParam)
        .then(() =>{
            fetchLoginUserProfile()
            .then((user) =>{
                const newUserInfo = {
                    id: user.id,
                    realName: user.realName,
                    email: user.email,
                    identity: user.identity,
                    location: user.location,
                    description: user.description,
                    profileSrc: `data:image/png;base64,${user.image}`
                }
                setUserInfo(newUserInfo);
                setLogin(true);
                nevigate("/");
            })

        })
        .catch((err) =>{
            alert("로그인에 실패하였습니다.");
        })
    }

    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="w-500 py-20 px-10 mt-60 bg-white border">
                <LabelSection asChild label="Email">
                    <Input id="tBox_email" onChange={(ev) => setEmail(ev.target.value)} type="email" placeholder="Email Address"></Input>
                </LabelSection>
                <LabelSection asChild label="Password" className="mt-4">
                    <Input id="tBox_pwd" onChange={(ev) => setPwd(ev.target.value)} type="password" placeholder="password"></Input>
                </LabelSection>
                {/* 버튼은 onClick 콜백 동작 불가 */}
                <Button onClick={doLogin} className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">로그인</Button>
                <div className="flex justify-center mt-4">
                    <Link to="/findPassword" className="text-black/65">비밀번호를 잊으셨나요?</Link>
                </div>
            </section>
        </main>
    );
}

export default Login;