import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";

const ChatDialog = (props) =>{
    const resource = {
        btnText: "쪽지 보내기",
        clickCallback: () =>{
            const chatContent = [...document.getElementById('chatContent').value];
            alert("save!")
        },
        initFn: () => {}
    }

    return (
        <Dialog>
            <DialogTrigger asChild {...props}></DialogTrigger>
            <DialogContent onPointerDownOutside={ev => ev.preventDefault()}>
                <DialogHeader>
                    <h1>쪽지 보내기</h1>
                </DialogHeader>
                <Textarea id="chatContent" className="mt-4 h-48 resize-none" placeholder="input your content"></Textarea>
                <DialogFooter>
                    <Button className="bg-[#6866EB]" onClick={resource.clickCallback} type="submit">{resource.btnText}</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default ChatDialog;








