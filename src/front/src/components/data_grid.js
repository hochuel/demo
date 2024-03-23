import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from "axios";
import useAsync from "./useAsync";
import Button from '@mui/material/Button';
import BasicModal from "../ui/BasicModal";
import {useState} from "react";

async function getDataList() {
    const response = await axios.get(
        '/api/dirFilesInfoList'
    );
    return response.data;
}

export default function StockDataTable() {

    const[modalOpenState, setModalOpenState] = useState(false);

    const [state, refetch] = useAsync(getDataList, [], false);
    const { loading, data: dataList, error } = state;

    if (loading) return <div>로딩중..</div>;
    if (error) return <div>에러가 발생했습니다</div>;
    if (!dataList) return null;

    const columns = [
        { field: 'stck_shrn_iscd', headerName: '종목코드', width: 150 },
        { field: 'hts_kor_isnm', headerName: '종목명', width: 150 },
        { field: 'stck_prdy_clpr', headerName: '현재가', width: 150 },
        { field: 'prdy_vol', headerName: '거래량', width: 150 },
        { field: 'stck_mxpr', headerName: '상한가', width: 150 },
        { field: 'stck_llam', headerName: '하한가', width: 150 },
    ];



    const onRowClick = (params) => {
        setModalOpenState(params.row.stck_shrn_iscd);
    }


    return (
        <div style={{ height: 500, width: '100%' }}>
            <DataGrid rows={dataList}
                      columns={columns}
                      getRowId={(row)=>row.stck_shrn_iscd}
                      onRowClick={onRowClick}
                      style={{ cursor: 'pointer' }}
            />
            <Button onClick={refetch}>다시 불러오기</Button>

            <BasicModal state={modalOpenState}/>

        </div>
    );
}