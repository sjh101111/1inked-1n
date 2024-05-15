import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Link } from "react-router-dom";

const FollowUser = ({realname, identity, image=null, email}) =>{
    return (
        <Link to="/userPage" state={{ email: email }} className="flex items-center gap-4">
            <Avatar className="w-10 h-10">
                {image ? (
                    <AvatarImage alt={realname} src={`data:image/png;base64,${image}`} />
                ) : (
                    <AvatarFallback>{realname.charAt(0)}</AvatarFallback>
                )}
            </Avatar>
            <div className="flex flex-col justify-center">
                <h2 className="font-bold text-lg">{realname}</h2>
                <span className="text-black/50 ">{identity}</span>
            </div>
        </Link>
    );
};

const FollowInfo = ({ follows, followers }) =>{
    console.log(followers, follows)

    return (
        <section className="w-full p-4 bg-white">
            {/* 헤더 */}
            <div className="flex h-12">
                <h2 className="w-1/2 text-lg font-bold m-auto">Follow</h2>
                <h2 className="w-1/2 text-lg font-bold m-auto">Follower</h2>
            </div>

            {/* content */}
            <div className="flex border-t">
                {/* Follow */}
                <section className="w-1/2 p-2 grid gap-4 mt-4">
                    {Array.isArray(follows) && follows.length > 0 ? (
                        follows.map(follow => (
                            <FollowUser key={follow.id} realname={follow.realname} identity={follow.identity} image={follow.image} email={follow.email} />
                        ))
                    ) : (
                        <p>No follows found</p>
                    )}
                </section>
                {/* Follower */}
                <section className="w-1/2 p-2 grid gap-4 mt-4">
                    {Array.isArray(followers) && followers.length > 0 ? (
                        followers.map(follower => (
                            <FollowUser key={follower.id} realname={follower.realname} identity={follower.identity} image={follower.image} email={follower.email} />
                        ))
                    ) : (
                        <p>No followers found</p>
                    )}
                </section>
            </div>
        </section>
    )
};


export default FollowInfo;