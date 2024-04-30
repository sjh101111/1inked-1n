import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Link } from "react-router-dom";

const FollowUser = ({username="temp", id="temp", img = null}) =>{
    return (
        <Link to="" className="flex items-center gap-4">
            <Avatar className="w-10 h-10">
                <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                <AvatarFallback>hi</AvatarFallback>
            </Avatar>
            <div className="flex flex-col justify-center">
                <h2 className="font-bold text-lg">username</h2>
                <span className="text-black/50 ">id</span>
            </div>
        </Link>
    );
};

const FollowInfo = () =>{
    const dummyFollowes = Array(4).fill(<FollowUser></FollowUser>);
    const dummyFollowers = Array(4).fill(<FollowUser></FollowUser>);

    // 팔로위, 팔로워 클릭시 해당 유저의 page로 이동
    return (
        <section className="w-full p-2">
            {/* 헤더 */}
            <div className="flex h-12">
                <h2 className="w-1/2 text-lg font-bold m-auto">Follow</h2>
                <h2 className="w-1/2 text-lg font-bold m-auto">Follower</h2>
            </div>

            {/* content */}
            <div className="flex border-t">
                {/* follow */}
                <section className="w-1/2 p-2 grid gap-4 mt-4">
                    {
                        dummyFollowes.map(item => item)
                    }
                </section>
                {/* follower */}
                <section className="w-1/2 p-2 grid gap-4 mt-4">
                    {
                        dummyFollowers.map(item => item)
                    }
                </section>
            </div>


        </section>
    )
};


export default FollowInfo;