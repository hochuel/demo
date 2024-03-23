import React from 'react';
import axios from 'axios';
import useAsync from './useAsync';
import StockChart from "./chartTest";


async function getFile(name) {
    const response = await axios.get(
        `/api/fileInfo/${name}`
    );
    return response.data;
}

function FileInfo({name}) {
    const [state] = useAsync(()=>getFile(name), [name]);

    const {loading, data: fileInfo, error } = state;

    if (loading) return <div>로딩중..</div>;
    if (error) return <div>에러가 발생했습니다</div>;
    if (!fileInfo) return null;
    return (
        <>
            <ul>
                <StockChart stockData={fileInfo}/>
            </ul>
        </>
    );
}

export default FileInfo;