import React from "react";
import "../style.scss";
import { NavLink } from "react-router-dom/cjs/react-router-dom.min";

function Header() {
    return (
        <nav className="header">
            <div className="header_logo">
                <NavLink to="/"><img src="../../ECON_RGB.svg" alt="Ericsson Logo"></img></NavLink>
            </div>
            <div className="header_title">
                <h1>Git mining dashboard</h1>
            </div>
        </nav>
    );
}

export default Header;