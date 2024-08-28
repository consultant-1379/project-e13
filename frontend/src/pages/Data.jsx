import React from "react";
import "../style.scss";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { Chart } from "react-google-charts";
import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Select, { SelectChangeEvent } from '@mui/material/Select';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
async function callAPI(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data
}

function convertDate(date, type) {
    if (!date) {
        return "Unknown Date";
    }
    let newDate = new Date(date);
    if (type === "list")
        newDate = (newDate.getDate() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getFullYear() + " (" + newDate.getHours() + ":" + newDate.getMinutes() + ":" + newDate.getSeconds() + ")");
    return newDate;
}

function parseStringifiedData(dataString) {
    try {
        let jsonString = dataString.replace(/'/g, '"');
        return JSON.parse(jsonString);
    } catch (e) {
        console.error("Error parsing stringified data:", e);
        return {};
    }
}

function getChartData(repo) {
    const smallContributors = parseStringifiedData(repo.smallContributors);
    const bigContributors = parseStringifiedData(repo.contributors);

    const allContributors = { ...bigContributors, ...smallContributors };


    const chartData = [
    ];
    for (const [author, commits] of Object.entries(allContributors)) {
        chartData.push([author, commits]);
    }
    return chartData;
}

const options = [{
    title: 'Lines added vs lines removed over time',
    hAxis: {
        title: 'Date',
        format: 'dd/MM/yy',
    },
    vAxis: {
        title: 'Commits',
    },
    legend: { position: "bottom" },
    colors: ['#0082f0', '#ff3232'],
}, {
    title: 'Authors vs commits',
    hAxis: {
        title: 'Number of commits',
    },
    vAxis: {
        title: 'Author',
    },
    legend: { position: "bottom" },
    colors: ['#0082f0', '#ff3232'],
}]

const types = ["ScatterChart", "BarChart"];
const graphTypes = ["Lines added vs lines removed over time", "Authors vs commits"];

function Data() {
    const location = useLocation();
    const { url } = location.state;
    const [chartType, setChartType] = useState(types[0]);
    const [chartData, setChartData] = useState(null);
    const [selectedData, setSelectedData] = useState(null);
    const [chartOptions, setChartOptions] = useState(options[0]);
    const [selectedRepo, setSelectedRepo] = useState(null);
    const [dataset, setData] = useState(null);
    const [repodata, setRepoData] = useState(null);
    const [selectedDate, setSelectedDate] = useState(0);
    const [selectedGraph, setSelectedGraph] = useState(0);

    useEffect(() => {
        callAPI(`http://localhost:8080/data/byUrl/?url=${url}`).then((data) => {
            if (Array.isArray(data)) {
                data.sort((a, b) => {
                    return new Date(a.date) < new Date(b.date);
                }
                );
                setData(data);
                setRepoData(data[0]);
                const parsed = parseStringifiedData(data[0].commitData);
                const test = parsed.map((commit) => {
                    let date = convertDate(commit.commitauthordate)
                    return [date, parseInt(commit.commitlinesadded), parseInt(commit.commitlinesremoved)];
                });
                
                setChartData(
                    [
                        [
                            ['Date', 'Lines added', 'Lines removed'],
                            ...test
                        ], [
                            ['Author', 'Number of commits'],
                            ...getChartData(data[0])
                        ]
                    ]
                );
                setSelectedRepo(data[0]);
            } else {
                console.error('API did not return an array:', data);
                setData([]);
            }
        });
    }, [url]);

    useEffect(() => {
        if (chartData == null) return;
        console.log(chartData)
        setSelectedData(chartData[0]);

    }, [chartData])


    const handleDataChange = (event) => {
        const selectedIndex = event.target.value;
        setSelectedRepo(dataset[selectedIndex]);
        setSelectedDate(selectedIndex)
    }


    const handleChartTypeChange = (event) => {
        const selectedIndex = event.target.value;
        setChartType(types[selectedIndex]);
        setChartOptions(options[selectedIndex]);
        setSelectedData(chartData[selectedIndex])
        setSelectedGraph(selectedIndex)
    }

    return (
        <div className="container">
            <Header />
            <div className="content">
                <div className="data-list">
                    <h1>General repo data</h1>
                    <FormControl fullWidth>
                        <InputLabel id="demo-simple-select-label">Date</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={selectedDate}
                            label="Date"
                            onChange={handleDataChange}
                        >
                            {Array.isArray(dataset) && dataset.length > 0 && dataset.map((repo, index) => (
                                <MenuItem key={repo.id} value={index}>
                                    {convertDate(repo.date, "list")}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    {selectedRepo && (
                        <ul key={selectedRepo.id}>
                            <li><h3>Report created: {convertDate(selectedRepo.date, "list")}</h3></li>
                            <li>Repository: <a href={selectedRepo.url} target="_blank" rel="noopener noreferrer">{selectedRepo.url}</a></li>
                            <li>Total commits: <b>{selectedRepo.commits}</b></li>
                            <li>Total Lines added: <b>{selectedRepo.linesAdded}</b></li>
                            <li>Total Lines removed: <b>{selectedRepo.linesRemoved}</b></li>
                            <li>Average change set: <b>{selectedRepo.averageChangeSet}</b></li>
                            <li>Maximum change set: <b>{selectedRepo.maxChangeSet}</b></li>
                            <li>Average code churn: <b>{selectedRepo.averageCodeChurn}</b></li>
                            <li>Maximum code churn: <b>{selectedRepo.maxCodeChurn}</b></li>
                            <li>Number of contributors: <b>{selectedRepo.contributorsNum}</b></li>
                            <li>Big contributors: {Object.entries(parseStringifiedData(selectedRepo.contributors)).map(([key, value], index) => {
                                if (index === Object.entries(parseStringifiedData(selectedRepo.contributors)).length-1) {
                                    return <b>{key}</b>
                                }
                                return <b>{key}, </b>
                            })}</li>
                            <li>Small contributors: {Object.entries(parseStringifiedData(selectedRepo.smallContributors)).map(([key, value], index) => {
                                if (index === Object.entries(parseStringifiedData(selectedRepo.smallContributors)).length-1) {
                                    return <b>{key}</b>
                                }
                                return <b>{key}, </b>
                            })}</li>
                            <li>Total hunks count: <b>{selectedRepo.hunksCount}</b></li>
                        </ul>
                    )}
                </div>


                <div className="graph">
                    <div className="graph-options">
                        <FormControl fullWidth>
                            <InputLabel id="demo-simple-select-label">Graph</InputLabel>
                            <Select
                                labelId="demo-simple-select-label"
                                id="demo-simple-select"
                                value={selectedGraph}
                                label="Date"
                                onChange={handleChartTypeChange}
                            >
                                {Array.isArray(graphTypes) && graphTypes.length > 0 && graphTypes.map((repo, index) => (
                                    <MenuItem key={repo.id} value={index}>
                                        {repo}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </div>
                    <div className="chart">
                        {repodata && selectedData &&
                            <Chart
                                width={'650px'}
                                height={'400px'}
                                chartType={chartType}
                                loader={<div>Loading Chart</div>}
                                data={selectedData}
                                options={chartOptions}
                            />
                        }
                    </div>
                </div>


            </div>
            <Footer />
        </div>
    );
}

export default Data;
