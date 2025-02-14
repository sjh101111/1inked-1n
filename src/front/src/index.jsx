import { useState, createContext, useEffect } from 'react';

const GlobalContext = createContext({
  isLogin: false,
  setLogin: () =>{}
});

const ContextProvider = ({children}) =>{
  const [isLogin, setLogin] = useState(false);

  return (
      <GlobalContext.Provider value={{
        isLogin,
        setLogin, 
      }}>
          {children}
      </GlobalContext.Provider>
  )
}


export {GlobalContext, ContextProvider}