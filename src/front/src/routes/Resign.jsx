import {useState, useContext} from "react";
import {useNavigate} from "react-router-dom";
import {GlobalContext} from "..";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import { AlertDialog, AlertDialogTrigger, AlertDialogContent,AlertDialogHeader, AlertDialogTitle, AlertDialogDescription, AlertDialogFooter, AlertDialogCancel, AlertDialogAction } from "@/components/ui/alert-dialog";
import LabelSection from "@/components/Layout/LabelSection";
import { withdrawReqParam } from "@/utils/Parameter";
import { logout, withdraw } from "@/utils/API";

const Resign = () => {
    const navigate = useNavigate();
    const [currentPassword, setCurrentPassword] = useState('');

    const handleResign = () => {
        //가데이터
        const reqParam = withdrawReqParam(currentPassword, true);

        withdraw(reqParam)
        .then((response) => {
            alert("회원탈퇴에 성공했습니다. 그동안 저희 서비스를 이용해주셔서 감사합니다.");
            logout();
            navigate("/login");
        });
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
                <AlertDialog>
                    <AlertDialogTrigger asChild>
                        <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600">탈퇴하기</Button>
                    </AlertDialogTrigger>
                    <AlertDialogContent>
                        <AlertDialogHeader>
                            <AlertDialogTitle>정말 회원 탈퇴하시겠어요?</AlertDialogTitle>
                            <AlertDialogDescription>
                                회원 탈퇴 이후엔 되돌릴 수 없어요. 신중하게 선택해주세요. 정말 탈퇴하시겠어요?
                            </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                            <AlertDialogCancel>취소</AlertDialogCancel>
                            <AlertDialogAction onClick={handleResign}>탈퇴</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
                
                <Button className="bg-[#6866EB] mt-4 w-full hover:bg-violet-600" onClick={handleGoBack}>
                    <div>돌아가기</div>
                </Button>
            </section>
        </main>
    );
};

export default Resign;