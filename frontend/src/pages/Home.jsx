import React from "react";
import Header from "../components/Header";
import Footer from "../components/Footer";
import "../style.scss";
import { NavLink } from "react-router-dom";
import { useState, useEffect } from "react";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

function getNumReports(dataset, commit) {
    let count = 0;
    for (let i = 0; i < dataset.length; i++) {
        if (dataset[i] === commit) {
            count++;
        }
    }
    return count;
}

function Home() {
    const [dataset, setData] = useState([]);
    const [uniqueSet, setUniqueSet] = useState([]);
    useEffect(() => {
        fetch('http://localhost:8080/data/')
            .then(response => response.json())
            .then(data => {
                console.log(data);
                setData(data)
                setUniqueSet([...new Set(data.map(commit => commit))])
            });
    }, []);
    return (
        <div className="container">
            <Header />
            <div className="content">
                <div className="table">
                    <Table sx={{ minHeight: 100 }}>
                        <TableHead>
                            <TableRow >
                                <TableCell align="center">Repository</TableCell>
                                <TableCell align="center">Number of reports</TableCell>
                                <TableCell align="center"></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {uniqueSet.map((row,index) => (
                                <TableRow key={index}>
                                    <TableCell align="center"><a href={row} target="_blank">{row}</a></TableCell>
                                    <TableCell align="center">{getNumReports(dataset, row)}</TableCell>
                                    <TableCell align="center"> <NavLink id="viewButton" to={{
                                        pathname: "/data",
                                        state: { url: row }
                                    }}>View</NavLink></TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default Home;
