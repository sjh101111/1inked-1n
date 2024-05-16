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
import Article from "@/components/Article.jsx";
import {Comment} from "@/components/Comment.jsx";
import ProtectedRoute from "@/routes/ProtectedRoute.jsx";
import Chat from "@/routes/Chat.jsx";

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
        element: (
            <ProtectedRoute>
                <Chat></Chat>
            </ProtectedRoute>
        )
      },
      {
        path: "/resign",
          element: (
              <ProtectedRoute>
                  <Resign></Resign>
              </ProtectedRoute>
          )
      },
      {
        path: "/mypage",
          element: (
              <ProtectedRoute>
                  <MyPage></MyPage>
              </ProtectedRoute>
          )
      },
      {
        path: "/userPage",
          element: (
              <ProtectedRoute>
                  <UserPage></UserPage>
              </ProtectedRoute>
          )
      },
      {
        path: "/resume",
          element: (
              <ProtectedRoute>
                  <Resume></Resume>
              </ProtectedRoute>
          )
      },
      // {
      //   path: "/test",
      //   element: <Test></Test>
      // },
      {
        path: "/newsinfo",
          element: (
              <ProtectedRoute>
                  <NewsPage></NewsPage>
              </ProtectedRoute>
          )
      }
]);

export default function App(){
    return (
      <CookiesProvider>
          <RouterProvider router={router} />
      </CookiesProvider>
    );
}