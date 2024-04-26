import { useState, createContext, useEffect } from 'react';

const GlobalContext = createContext({});

const ContextProvider = ({children}) =>{
    return (
        <GlobalContext.Provider value={{}}>
            {children}
        </GlobalContext.Provider>
    )
}


export {GlobalContext, ContextProvider}