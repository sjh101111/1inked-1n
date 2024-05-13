import { useState, createContext, useEffect } from 'react';

const GlobalContext = createContext({
  isLogin: false,
  userInfo: {},
  setLogin: () =>{},
  setUserInfo: () =>{}
});

const ContextProvider = ({children}) =>{
  // 생성자
  const [isLogin, setLogin] = useState(false);
  const [userInfo, setUserInfo] = useState({});

  return (
      <GlobalContext.Provider value={{
        isLogin,
        userInfo,
        setLogin, 
        setUserInfo
      }}>
          {children}
      </GlobalContext.Provider>
  )
}


export {GlobalContext, ContextProvider}