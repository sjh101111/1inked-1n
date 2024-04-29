

import { useState, useContext } from "react"; 
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import LabelSection from "@/components/Layout/LabelSection";


const PasswordChange = () => {

    const { isLogin, setLogin } = useContext(GlobalContext);
    //라우팅 네비게이터
    const nevigate = useNavigate();

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
            if (newPassword !== verifyPassword) {
                alert("새 비밀번호를 확인하세요!");
            } else {
                //대충 비밀번호 변경 API 통신
                //만약 비밀번호 변경을 성공했다고 답이 돌아온다면
                alert("비밀번호 변경이 완료되었습니다! 새로운 비밀번호로 다시 로그인 해보세요!")
                setLogin(false);
                navigate("/login");
            }
        }

        
    }


    return (
        <main id="login-wrap" className="flex flex-col min-h-screen items-center">
            <section className="flex flex-col items-center w-500 py-20 px-10 mt-60 bg-white border">

                <h1>1INKED 1N</h1>
                <h3>비밀번호 변경</h3>

                <LabelSection asChild label="Current Password" className="mt-4">
                    <Input id="cur_pwd" type="password" placeholder="current password"
                    value = {currentPassword} onChange={(p => setCurrentPassword(p.target.value))}/>
                </LabelSection>


                <LabelSection asChild label="New Password" className="mt-4">
                    <Input id="new_pwd" type="password" placeholder="new password"
                    value = {newPassword} onChange={(p => setNewPassword(p.target.value))}/>
                </LabelSection>

                <LabelSection asChild label="Verify Password" className="mt-4">
                    <Input id="veri_pwd" type="password" placeholder="verify password"
                    value = {verifyPassword} onChange={(p => setVerifyPassword(p.target.value))}/>
                </LabelSection>
              
                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handlePasswordChange}>
                    <div>비밀번호 변경</div>
                </Button>
            </section>
        </main>
    );
};

export default PasswordChange;