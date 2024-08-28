import React from "react";
import Home from "./pages/Home";
import Data from "./pages/Data";
import { BrowserRouter as Switch, Route } from "react-router-dom";


function App() {
    return (
        <main>
            <Switch>
                <Route path="/" exact component={Home} />
                <Route path="/data" component={Data} />
            </Switch>
        </main>
    );
}


export default App;
