import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

interface AlertProb{
    openModal : boolean;
    rightButton : {state:false, name:'', callFun:object};
    leftButton : {state:false, name:'', callFun:object};
    title?: string;
    msg? : string;
}

export default function AlertDialog(prop: AlertProb) {

    let rightButton;
    let leftButton;

    if(prop.rightButton.state){

        rightButton = <Button onClick={prop.rightButton.callFun}>{prop.rightButton.name}</Button>;
    }

    if(prop.leftButton.state){
        leftButton = <Button onClick={prop.leftButton.callFun}>{prop.leftButton.name}</Button>;
    }

    return (
        <React.Fragment>
            <Dialog
                open={prop.openModal}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {prop.title ? prop.title : '제목'}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {prop.msg ? prop.msg : '메세지내용 없음'}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    {leftButton}{rightButton}
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}