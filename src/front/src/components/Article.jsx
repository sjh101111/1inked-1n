import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import { Textarea } from "@/components/ui/textarea";
import FileInput from "@/components/FileInput";
import { Button } from "@/components/ui/button";


const Article = () =>{
    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }

    return (
        <Dialog>
            <DialogTrigger>Open</DialogTrigger>
            <DialogContent onPointerDownOutside={ev => ev.preventDefault()}>
                <DialogHeader>
                    <div className="flex items-center gap-4">
                        <Avatar className="w-8 h-8">
                            <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                            <AvatarFallback>hi</AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col justify-center">
                            <h2 className="font-bold text-lg">username</h2>
                            <span className="text-black/50 ">id</span>
                        </div>
                    </div>
                </DialogHeader>
                <Textarea className="mt-4 h-48 resize-none" placeholder="input your content"></Textarea>
                <FileInput></FileInput>
                <DialogFooter>
                    <Button className="bg-[#6866EB]" onClick={resource.clickCallback} type="submit">{resource.btnText}</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default Article;