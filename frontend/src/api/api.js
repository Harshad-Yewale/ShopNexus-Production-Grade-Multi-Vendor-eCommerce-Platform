import axios from "axios";
import store from "../store/reducers/store";
import { logOutUser } from "../store/actions";

const api = axios.create({
    baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
    withCredentials: true,
});

let csrfToken = null;

export const primeCsrfCookie = async () => {
    try {
        const { data } = await api.get("/public/csrf", { skipAuthRedirect: true });
        csrfToken = data.csrfToken;
    } catch {
    }
};

api.interceptors.request.use((config) => {
    if (csrfToken) {
        config.headers["X-XSRF-TOKEN"] = csrfToken;
    }
    return config;
});

export const setupInterceptors = (navigate) => {
    api.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response?.status === 401 && !error.config?.skipAuthRedirect) {
                store.dispatch(logOutUser(navigate));
            }

            return Promise.reject(error);
        }
    );
};

export default api;