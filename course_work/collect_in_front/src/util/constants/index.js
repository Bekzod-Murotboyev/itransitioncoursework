import {ENG} from "./language";

const HOST = '3.95.203.15'
const BACK_HOST = HOST + ':8080'
const FRONT_HOST = HOST + ':3000'


export const BASE_URL = 'http://' + BACK_HOST;

export const API_BASE_URL = BASE_URL + '/api/v1';

export const SOCKET_URL = 'ws://' + BACK_HOST + '/ws';


export const OAUTH2_REDIRECT_URI = 'http://' + FRONT_HOST + '/oauth2/redirect'

export const GOOGLE_AUTH_URL = BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;


export const ACCESS_TOKEN = 'access-token';
export const ITEM_DATA = 'item-data';
export const LANGUAGE = 'collect-lang';
const lan = localStorage.getItem(LANGUAGE)
export const PAGE_AUTHENTICATION = lan === ENG ? 'Authentication' : 'Аутентификация'
export const PAGE_COLLECTION = lan === ENG ? 'Collection' : 'Коллекция'


export const UN_BLOCK = lan === ENG ? 'ACTIVE' : 'АКТИВНЫЙ'
export const BLOCK = lan === ENG ? 'BLOCKED' : 'ЗАБЛОКИРОВАНО'
export const DELETE = lan === ENG ? 'DELETED' : 'УДАЛЕНО'
export const GIVE_PERMISSION = lan === ENG ? 'Give Permission' : 'Дать разрешение'
export const GET_PERMISSION = lan === ENG ? 'Get Permission' : 'Получить разрешение'