import { Button } from "@/components/ui/button";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "..";

const Login = () =>{
    //라우팅 네비게이터
    const nevigate = useNavigate();

    //글로벌 컨텍스트
    const {isLogin, setLogin} = useContext(GlobalContext);

    const doLogin = () =>{
        setLogin(true);
        nevigate("/");
    }

    const goSignup = () =>{
        nevigate("/signup")
    }

    return (
        <main className="flex h-screen">
            <section className=" flex-grow-[6] bg-[#6866EB] px-16 py-24 text-white ">
                <h1 className="text-6xl font-bold">About Us</h1>
                <p className="mt-48 text-4xl font-bold grid gap-4">
                    <h2>1</h2>
                    <h2>2</h2>
                    <h2>3</h2>
                    <h2>4</h2>
                </p>
            </section>
            <section className="flex-grow-[4] login flex flex-col items-center justify-center">
                <div className="w-full text-4xl font-bold text-white">
                    <h2 className="text-center text-black">Get Started</h2>
                    <div className="flex justify-center gap-4 mt-4">
                        <Button className="bg-[#6866EB] w-48 hover:bg-violet-600">
                            <div onClick={doLogin}>
                                Login
                            </div>
                        </Button>
                        <Button className="bg-[#6866EB] w-48 hover:bg-violet-600">
                            <div onClick={goSignup}>
                                Signup
                            </div>
                        </Button>
                    </div>
                </div>
            </section>
        </main>
    );
}

export default Login;