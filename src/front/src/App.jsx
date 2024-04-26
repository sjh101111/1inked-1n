import { GlobalContext, ContextProvider } from ".";
import { CookiesProvider } from 'react-cookie';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Main from "./routes/Main";
import Login from "./routes/Login";
import Signup from "./routes/Signup";


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