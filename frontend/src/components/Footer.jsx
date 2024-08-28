import React from "react";

const year = new Date().getFullYear();

function Footer() {
    return (
        <footer>
            <div className="footer"><p>&copy; Maintained by Git Gud {year}</p></div>
        </footer>
    );
}

export default Footer;