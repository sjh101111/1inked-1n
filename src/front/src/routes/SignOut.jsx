

import { useState, useContext } from "react"; 
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Separator}  from "@/components/ui/separator"
import LabelSection from "@/components/Layout/LabelSection";



const SignOut = () => {

    const { isLogin, setLogin } = useContext(GlobalContext);
    //라우팅 네비게이터
    const navigate = useNavigate();

    // State for input values
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');

    // Handle password change submission
    const handlePasswordChange = async () => {
        const verifyCurrentPassword = async (password) => {
            // 현재 비밀번호 가져와서 검증하는 함수(API로 사용)
            return password === "expectedPassword";
        };
        //새 비밀번호 틀릴시
        const isValidCurrentPassword = await verifyCurrentPassword(currentPassword);
        if (!isValidCurrentPassword) {
            alert("현재 비밀번호가 틀렸습니다.");
        }  else {
            //현재 비밀번호 검증
            if (currentPassword !== verifyPassword) {
                alert("재입력 비밀번호를 확인하세요");
            } else {
                alert("회원 탈퇴가 완료됐습니다. 저희 서비스를 이용해주셔서 감사합니다.")
                setLogin(false);
                navigate("/");
            }
        }
    }

    const handleGoBack = () => {
        if (window.history.state && window.history.state.idx > 0) {
            navigate(-1);
        }else{
            navigate("/");
        }
    };


    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="flex flex-col items-center w-500 py-20 px-10 mt-60 bg-white border">
                <h1>1INKED 1N</h1>
                <h3>회원 탈퇴</h3>
                <LabelSection asChild label="Current Password" className="mt-4">
                    <Input id="cur_pwd" type="password" placeholder="current password"
                    value = {currentPassword} onChange={(p => setCurrentPassword(p.target.value))}/>
                </LabelSection>

                <LabelSection asChild label="Verify Password" className="mt-4">
                    <Input id="veri_pwd" type="password" placeholder="verify password"
                    value = {verifyPassword} onChange={(p => setVerifyPassword(p.target.value))}/>
                </LabelSection>
              
                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handlePasswordChange}>
                    <div>회원 탈퇴</div>
                </Button>

                <Separator className ="mt-3" />

                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handleGoBack}>
                    <div>뒤로가기</div>
                </Button>
            </section>
        </main>
    );
};

export default SignOut;