import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import { Textarea } from "@/components/ui/textarea";
import FileInput from "@/components/FileInput";
import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import SearchIcon from "@/components/svg/SearchIcon";
import { Input } from "@/components/ui/input";
import Article from "@/components/Article";

const Test =  () => {
    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }


    return (
        <header className="flex items-center border-b dark:border-gray-800 overflow-hidden">
            <div className="flex items-center px-4 py-2 justify-between overflow-hidden w-full">
                <div className="flex items-center gap-2 min-w-[500px]">
                    <Link to="/">
                        <h2>Logo</h2>
                    </Link>
                    <div className="flex items-center rounded-sm border gap-2 pl-2 w-full overflow-hidden">
                        <SearchIcon></SearchIcon>
                        <Input className="flex-grow focus-visible:ring-transparent border-0" type="text" placeholder="원하는 키워드를 입력해보세요!"></Input>
                    </div>
                </div>

                <div className="flex items-center gap-4 min-w-[350px]">
                    <Link to="/newsinfo">뉴스</Link>
                    <Link to="/resumeAi">Resume</Link>
                    <Link to="/chat">쪽지</Link>
                    <div className="flex items-center ml-2 gap-2">
                        <Avatar className="w-10 h-10">
                            <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                            <AvatarFallback>hi</AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col justify-center">
                            <h2 className="font-medium text-base">username</h2>
                            <span className="text-sm text-black/50 ">id</span>
                        </div>
                    </div>
                </div>
            </div>
            <div className="flex px-4 gap-4 items-center justify-center border-l w-2/12">
                <Article>
                    <Button className="bg-[#6866EB] hover:bg-violet-600">Create</Button>
                </Article>
                <Button className="bg-[#6866EB] hover:bg-violet-600">Logout</Button>
            </div>
        </header>
        // <Dialog>
        //     <DialogTrigger>Open</DialogTrigger>
        //     <DialogContent onPointerDownOutside={ev => ev.preventDefault()}>
        //         <DialogHeader>
        //             <div className="flex items-center gap-4">
        //                 <Avatar className="w-8 h-8">
        //                     <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
        //                     <AvatarFallback>hi</AvatarFallback>
        //                 </Avatar>
        //                 <div className="flex flex-col justify-center">
        //                     <h2 className="font-bold text-lg">username</h2>
        //                     <span className="text-black/50 ">id</span>
        //                 </div>
        //             </div>
        //         </DialogHeader>
        //         <Textarea className="mt-4 h-48 resize-none" placeholder="input your content"></Textarea>
        //         <FileInput></FileInput>
        //         <DialogFooter>
        //             <Button className="bg-[#6866EB]" onClick={resource.clickCallback} type="submit">{resource.btnText}</Button>
        //         </DialogFooter>
        //     </DialogContent>
        // </Dialog>
    );
};

export default Test;