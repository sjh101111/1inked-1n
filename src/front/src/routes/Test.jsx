import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import { Textarea } from "@/components/ui/textarea";
import FileInput from "@/components/FileInput";
import { Button } from "@/components/ui/button";

const Test =  () => {
    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }
    

    useEffect(() =>{
       getAccessTokenInfo(); 
       setLogin(true);
    },[]);

    const userProfileDemo = () =>{

        fetchLoginUserProfile()
        .then((data) => {console.log(data)});
    }

    const userWithdraw = (bool) =>{
        const reqParam = withdrawReqParam("Dlxogml!135", bool);

        withdraw(reqParam)
        .then(data => console.log(data));
    }

    const saveUserProfile = () =>{
        const reqParam = saveProfileReqParam("123","서울","ㄷㅈ매럊믇ㄹ",file);

        saveProfile(reqParam)
        .then(response => console.log(response));
    }

    const showMainFeedArticles = () =>{
        readMainFeedArticles()
        .then((data) => {
            console.log(data);
        });
    }

    const showAllMyArticles = () =>{
        readAllMyArticle()
        .then((response) => {
            setArticles([...response]);
            setArticleItems(getArticleItems([...response]));
            console.log(response);
        }
        );
    }

    const createMainFeedArticle = () =>{
        const reqParam = createArticleReqParam("안녕하세요", articleFiles);

        createArticle(reqParam)
        .then(response =>{
            console.log(response);
        });
    }

    const updateWritenArticle = () =>{
        console.log(articles);
        if(articles.length == 0){
            alert("내 아티클 조회API를 호출해주세요.");
            return ;
        }

        const firstArticleId = articles[0].id;
        //현재 첨부한 파일들로 reqParam 불러온다.
        const reqParam = updateArticleReqParam(Math.random().toString(36).substring(2, 12), articleFiles);
        updateArticle(firstArticleId, reqParam)
        .then(data => console.log(data));
    }

    const deleteWritenArticle = () =>{
        if(articles.length == 0){
            alert("조회된 Article이 없습니다.");
            return ;
        }

        const deleteArticleId = articles[0].id;
        deleteArticle(deleteArticleId)
        .then(data => console.log(data));
    }

    const showMyFollows = () =>{
        getFollows()
        .then((data) => console.log(data));
    }

    const showMyFollowers = () => {
        getFollowers()
        .then((data) => console.log(data));
    }

    const addFollower = () =>{
        const reqParam = followUserReqParam("user_0254478418623611")

        followUser(reqParam)
        .then((data) => console.log(data));
    }

    const removeFollower = () =>{
        const unfollowUserId = "user_0254478418623611";

        unFollowUser(unfollowUserId)
        .then((response) => {
            alert("성공");
        })
        .catch(err =>{
            console.log(err)
        });
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

export default Test;