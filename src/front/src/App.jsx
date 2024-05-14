import { GlobalContext, ContextProvider } from ".";
import { CookiesProvider } from 'react-cookie';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Main from "./routes/Main";
import Login from "./routes/Login";
import Signup from "./routes/Signup";
import Test from "./routes/Test";
import ChatBox from "./components/ChatBox";
import MyPage from "./routes/Mypage";
import UserPage from "./routes/UserPage";
import PasswordFind from "./routes/PasswordFind";
import Resign from "./routes/Resign";
import NewsPage from "./routes/NewsPage";
import Resume from "@/routes/Resume.jsx";




const router = createBrowserRouter([
      {
        path: "/",
        element: <Main></Main>
      },
      {
        path: "/login",
        element: <Login></Login>
      },
      {
        path: "/signup",
        element: <Signup></Signup>
      },
      {
        path: "/findPassword",
        element: <PasswordFind></PasswordFind>
      },
      {
        path: "/chat",
        element: <ChatBox></ChatBox>
      },
      {
        path: "/resign",
        element: <Resign></Resign>
      },
      {
        path: "/mypage",
        element: <MyPage></MyPage>
      },
      {
        path: "/userPage",
        element: <UserPage></UserPage>
      },
      {
        path: "/resume",
        element: <Resume></Resume>
      },
      {
        path: "/test",
        element: <Test></Test>
      },
      {
        path: "/newspage",
        element: <NewsPage></NewsPage>
      }
]);

export default function App(){
    return (
        <ContextProvider>
            <CookiesProvider>
                <RouterProvider router={router} />
            </CookiesProvider>
        </ContextProvider>
    );
}