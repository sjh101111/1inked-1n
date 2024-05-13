import { GlobalContext, ContextProvider } from ".";
import { CookiesProvider } from 'react-cookie';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Main from "./routes/Main";
import Login from "./routes/Login";
import Signup from "./routes/Signup";
import Test from "./routes/Test";


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
        path: "/test",
        element: <Test></Test>
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