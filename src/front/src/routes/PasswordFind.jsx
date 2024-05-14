import { useState, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
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
import { changePassword, fetchPasswordQuestions } from "@/utils/API";
import { getPasswordQuestionItems } from "@/utils/Items";
import { correctRegxEmail, correctRegxPwd } from "@/utils/common";
import { changePasswordReqParam } from "@/utils/Parameter";

const PasswordFind = () => {
    //라우팅 네비게이터
    const nevigate = useNavigate();

    //비밀번호 찾기 질문 Items
    const [passwordQuestions, setPasswordQuestions] = useState([]);
    //Email 주소
    const [email, setEmail] = useState("");
    //선택한 비밀번호 찾기 질문
    const [selectQuestion, setSelectQuestion] = useState(null);
    //입력한 비밀번호 찾기 질문 응답
    const [passwordQuestionAnswer, setPasswordQuestionAnswer] = useState("");
    //새로운 비밀번호
    const [newPassword, setNewPassword] = useState("");

    useEffect(() =>{
        fetchPasswordQuestions()
        .then(resultQuestions =>{
            setPasswordQuestions(getPasswordQuestionItems(resultQuestions));
        })
    },[]);
    
    const findPassword = () =>{
        if(!correctRegxEmail(email)){
            alert("이메일을 확인해주세요");
            return ;
        }

        if(selectQuestion === null){
            alert("비밀번호 찾기 질문을 선택해주세요");
            return ;
        }

        if(passwordQuestionAnswer === ""){
            alert("비밀번호 찾기 응답을 입력해주세요");
            return ;
        }

        if(!correctRegxPwd(newPassword)){
            alert("비밀번호 형식이 올바르지 않습니다.");
            return ;
        }

        const reqParam = changePasswordReqParam(email, selectQuestion, passwordQuestionAnswer, newPassword);

        changePassword(reqParam)
        .then((response) => {
            alert(response + "\n 다시 로그인해주세요!");
            nevigate("/");
        })
        .catch((err) =>{
            alert("비밀번호 변경에 실패했습니다.");
        });
    }


    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="flex flex-col items-center w-500 py-20 px-10 mt-60 bg-white border">
                <h1>1INKED 1N</h1>
                <h3>비밀번호 변경</h3>
                <LabelSection asChild label="Email">
                    <Input onChange={ev => setEmail(ev.target.value)} type="email" placeholder="Email Address"></Input>
                </LabelSection>

                <LabelSection asChild label="Password recovery Question" className="mt-4">
                    <Select onValueChange={val => setSelectQuestion(val)}>
                        <SelectTrigger>
                            <SelectValue placeholder="Question List" />
                        </SelectTrigger>
                        <SelectContent>
                            {
                                passwordQuestions
                            }
                        </SelectContent>
                    </Select>

                    <Input onChange={ev => setPasswordQuestionAnswer(ev.target.value)} type="input" placeholder="your answer"></Input>
                </LabelSection>

                <LabelSection asChild label="new Password" className="mt-4">
                    <Input onChange={ev => setNewPassword(ev.target.value)} type="password" placeholder="enter new password"></Input>
                </LabelSection>

                {/* 버튼은 onClick 콜백 동작 불가 */}
                <Button onClick={findPassword} className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">비밀번호 변경</Button>
            </section>
        </main>
    );
};

export default PasswordFind;