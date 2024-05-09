import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectTrigger, SelectValue, SelectContent, SelectGroup } from "@/components/ui/select";
import LabelSection from "@/components/Layout/LabelSection";
import { correctRegxEmail, correctRegxPwd } from "@/utils/common";
import { signup } from "@/utils/API";
import { signupReqParam } from "@/utils/Parameter";

const Signup = () =>{
    //라우팅 네비게이터
    const nevigate = useNavigate();

    const [passwordQuestions, setPasswordQuestions] = useState([]);
    const [pwdQId, setpwdQId] = useState("1");
    // 유저정보
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [pwd, setPwd] = useState("");
    const [pwdConfirm, setPwdConfirm] = useState("");
    const [pwdQAnswer, setPwdQAnswer] = useState("");

    //메서드
    const doSignup = () =>{
        if(!username){
            alert("유저명을 입력해주세요");
            return ;
        }

        if(!email){
            alert("이메일을 입력해주세요");
            return ;
        }

        if(!pwd || !pwdConfirm || pwd != pwdConfirm){
            alert("비밀번호, 비밀번호 입력을 확인해주세요");
            return ;
        }

        if(!correctRegxEmail(email)){
            alert("이메일 형식이 올바르지 않습니다.");
            return ;
        }

        if(!correctRegxPwd(pwd)){
            alert("비밀번호는 영어 대문자,소문자,특수기호,숫자를 반드시 포함한 8 ~ 16자리 문자여야합니다.");
            return ;
        }

        if(!pwdQId){
            alert("비밀번호 찾기 질문을 선택해주세요.");
            return ;
        }

        const reqParam = signupReqParam(username, email, pwd, pwdQId, pwdQAnswer);

        console.log(reqParam);

        // signup API 호출

        signup(reqParam)
        .then(() =>{
            alert("회원가입이 정상적으로 완료되었습니다.");
            nevigate("/");
        })
        .catch(err =>{
            console.log(err);
        });
    }

    const doLogin = () =>{
        nevigate("/");
    };

    return (
        <main className="flex flex-col min-h-screen items-center">
            <section className="w-500 py-16 px-10 mt-16 bg-white border">
                <h1 className="logo text-center">logo</h1>
                <h2 className="mt-4 text-center font-bold text-xl">가입을 통해 자소서 첨삭과 전문가 매칭 서비스를 누려보세요</h2>
                <LabelSection label="Username" asChild className="mt-4">
                        <Input placeholder="Enter your username" onChange={(ev) => setUsername(ev.target.value)}></Input>
                </LabelSection>
                <LabelSection asChild label="이메일" className="mt-2">
                    <Input type="email" placeholder="이메일을 입력해주세요." onChange={(ev) => setEmail(ev.target.value)}></Input>
                </LabelSection>
                <LabelSection asChild label="비밀번호" className="mt-2">
                    <Input type="password" placeholder="비밀번호를 입력해주세요." onChange={(ev) => setPwd(ev.target.value)}></Input>
                </LabelSection>
                <LabelSection asChild label="비밀번호확인" className="mt-2">
                    <Input type="password" placeholder="비밀번호를 입력해주세요." onChange={(ev) => setPwdConfirm(ev.target.value)}></Input>
                </LabelSection>
                <LabelSection asChild label="비밀번호 찾기 질문" className="mt-2">
                    <Select onValueChange={setpwdQId}>
                        <SelectTrigger>
                            <SelectValue placeholder="선택"></SelectValue>
                        </SelectTrigger>
                        <SelectContent>
                            <SelectGroup>
                                {
                                    passwordQuestions.map(passwordQuestion => <SelectItem key={passwordQuestion.passwordQuestionId} value={passwordQuestion.passwordQuestionId}>{passwordQuestion.passwordQuestion}</SelectItem>)
                                }
                            </SelectGroup>
                        </SelectContent>
                    </Select>
                </LabelSection>
                <LabelSection asChild label="비밀번호 찾기 질문 응답" className="mt-2">
                    <Input type="text" placeholder="비밀번호 찾기 질문 응답을 입력해주세요." onChange={(ev) => setPwdQAnswer(ev.target.value)}></Input>
                </LabelSection>
                <Button asChild className="text-white bg-[#6866EB] mt-4 w-full hover:bg-violet-600">
                    <div onClick={doSignup}>
                        Signup
                    </div>
                </Button>
                <div className="flex flex-col justify-center mt-4">
                    <h3 className="text-center text-lg font-bold">이미 계정이 있으신가요?</h3>
                    <Button asChild className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">
                        <div onClick={doLogin}>로그인</div>
                    </Button>
                </div>
            </section>
        </main>
    );
};

export default Signup;