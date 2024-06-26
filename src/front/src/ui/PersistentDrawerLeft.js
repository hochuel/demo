import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import CssBaseline from '@mui/material/CssBaseline';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import axios from "axios";
import {useCallback, useEffect, useState} from "react";
import AlertDialog from "./AlertDialog";
import ModalUnstyled from "./Register";


const instance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": `application/json;charset=UTF-8`,
        "Accept": "application/json",

        "Access-Control-Allow-Origin": `http://localhost:3000`,
        'Access-Control-Allow-Credentials':"true",
    }
});

async function getCheckConfig(){
    try {
        const response = await instance.get("http://localhost:8080/sample/getCheckConfig");
        //console.log(JSON.stringify(response.data));
        return response.data;
    }catch(error){
        console.error('Error :', error)
        return {cd:'9999', msg:error}
    }
}



const drawerWidth = 240;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        flexGrow: 1,
        padding: theme.spacing(3),
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        marginLeft: `-${drawerWidth}px`,
        ...(open && {
            transition: theme.transitions.create('margin', {
                easing: theme.transitions.easing.easeOut,
                duration: theme.transitions.duration.enteringScreen,
            }),
            marginLeft: 0,
        }),
    }),
);

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
    transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: `${drawerWidth}px`,
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));


const alertInitialState = {
    open : false,
    title : '',
    message : '',
    rightButton : '',
    leftButton : ''
}



export default function PersistentDrawerLeft() {
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);

    const [alertOption, setAlertOption] = useState(alertInitialState);
    const [configState, setConfigState] = useState(false);

    const [checkConfig, setCheckConfig] = useState({cd: '', mg: ''})

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const closeAlert = () =>{
        setAlertOption({open:false});
    }
    const openModalHandler = useCallback(()=>{
        alertInitialState.open = true;
        alertInitialState.title = "알림";
        alertInitialState.message = "설정된 값이 없습니다.";
        alertInitialState.rightButton = {state:true, name:'설정화면', callFun:closeModalHandler};
        alertInitialState.leftButton = {state:false, name:'', callFun:null};
        setAlertOption(alertInitialState);
    }, [])


    const closeModalHandler = ()=> {
        setAlertOption({open:false});

        setConfigState(true);
    }

    const closeRegViewModal = () => {
        setConfigState(false);

        alertInitialState.open = true;
        alertInitialState.title = "확인";
        alertInitialState.message = "저장 하였습니다.";
        alertInitialState.rightButton = {state:false, name:'', callFun:null};
        alertInitialState.leftButton = {state:true, name:'닫기', callFun:closeAlert};
        setAlertOption(alertInitialState);
    }



    const fetchData = useCallback(async ()=>{
        const data = await getCheckConfig();
        setCheckConfig(data);
        if (checkConfig.cd !== '0000') {
            openModalHandler();
        }
        console.log(data);
    }, [checkConfig.cd, openModalHandler]);

    useEffect(() => {
        fetchData();
    },[fetchData])

    return (

        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar position="fixed" open={open}>
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        sx={{ mr: 2, ...(open && { display: 'none' }) }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" noWrap component="div">
                        수 익 창 출
                    </Typography>
                </Toolbar>
            </AppBar>
            <Drawer
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: drawerWidth,
                        boxSizing: 'border-box',
                    },
                }}
                variant="persistent"
                anchor="left"
                open={open}
            >
                <DrawerHeader>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                    </IconButton>
                </DrawerHeader>
                <Divider />
                <List>
                    {['Inbox', 'Starred', 'Send email', 'Drafts'].map((text, index) => (
                        <ListItem key={text} disablePadding>
                            <ListItemButton>
                                <ListItemIcon>
                                    {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
                                </ListItemIcon>
                                <ListItemText primary={text} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
                <Divider />
                <List>
                    {['All mail', 'Trash', 'Spam'].map((text, index) => (
                        <ListItem key={text} disablePadding>
                            <ListItemButton>
                                <ListItemIcon>
                                    {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
                                </ListItemIcon>
                                <ListItemText primary={text} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Drawer>
            <Main open={open}>
                <DrawerHeader />
                <Typography paragraph>
                    메인화면
                </Typography>
            </Main>

            <div>
                {alertOption.open && <AlertDialog title={alertOption.title}
                                             msg={alertOption.message}
                                             openModal={alertOption.open}
                                             rightButton={alertOption.rightButton}
                                             leftButton={alertOption.leftButton}/>}

                {configState && <ModalUnstyled openModal={configState} callFun={closeRegViewModal}/>}
            </div>
        </Box>
    );
}