import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import {useEffect} from "react";
import {useState} from "react";
import Button from "@mui/material/Button";
import FileInfo from "../components/FileInfo";

const style = {
    position: 'absolute',
    top: '50%',
    left: '20%',
    transform: 'translate(-50%, -50%)',
    width: 200,
    height : 550,
    //bgcolor: '#999',
    //border: '2px solid #000',
    //boxShadow: 24,
    p: 2,
};



export default function BasicModal({state}) {
    const [open, setOpen] = useState('');
    const handleOpen = (state) => {
        setOpen(true);
    };
    const handleClose = () => {

        setOpen(false);
    };

    useEffect(() => {

        if({state}){
            handleOpen(state);
        }

        return () => {
            state = false;
        };

    }, [state]);

    return (
        <div>
            <Modal open={open} onClose={handleClose}>
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Text in a modal
                    </Typography>
                    {state && <FileInfo name={state}/>}
                </Box>
            </Modal>
        </div>
    );
}