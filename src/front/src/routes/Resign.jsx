import {useState, useContext} from "react";
import {useNavigate} from "react-router-dom";
import {GlobalContext} from "..";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import LabelSection from "@/components/Layout/LabelSection";

const Resign = () => {
    const { setLogin } = useContext(GlobalContext);
    const navigate = useNavigate();
    const [currentPassword, setCurrentPassword] = useState('');

    const handleResign = () => {
        if (window.confirm("정말로 회원을 탈퇴하시겠습니까?")) {
            // 회원 탈퇴 로직을 실행
            alert("회원 탈퇴가 완료되었습니다.");
            setLogin(false);
            navigate("/login");
        }
    };

    // Navigate to mypage
    const handleGoBack = () => {
        navigate("/mypage");
    };

    return (
        <main id="login-wrap" className="flex justify-center items-center h-screen">
            <section className="flex flex-col items-center w-500 py-20 px-10 bg-white border">
                <h1>1INKED 1N</h1>
                <h3>회원 탈퇴</h3>
                <LabelSection asChild label="Password" className="mt-4">
                    <Input id="cur_pwd" type="password" placeholder="current password"
                           value={currentPassword} onChange={(e => setCurrentPassword(e.target.value))}/>
                </LabelSection>
                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handleResign}>
                    <div>탈퇴하기</div>
                </Button>
                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handleGoBack}>
                    <div>돌아가기</div>
                </Button>
            </section>
        </main>
    );
};

export default Resign;