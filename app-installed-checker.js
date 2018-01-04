import { Linking, Platform } from 'react-native';
import { APP_LIST } from './app-list';
import CheckPackageInstallation from './android';

class AppInstalledChecker {

    static getAppList() {
        return Object.keys(APP_LIST);
    }

    static checkPackageName(packagename) {
        return new Promise((resolve, reject) => {
            CheckPackageInstallation.isPackageInstalled(packagename, (isInstalled) => {
                resolve(isInstalled);
            });
        });
    }

    static checkURLScheme(proto, query) {
        return new Promise((resolve, reject) => {
            Linking
                .canOpenURL(proto + '://' + query || '')
                .then((isInstalled) => {
                    console.log('isInstalled', isInstalled);
                    resolve(isInstalled);
                })
                .catch((err) => {
                    console.log('erroer', err);
                    reject(err);
                });
        });
    }

    static isAppInstalled(key) {
        return Platform.select({
            ios: () => { return this.isAppInstalledIOS(key); },
            android: () => { return this.isAppInstalledAndroid(key); }
        })();
    }

    static isAppInstalledAndroid(key) {
        return this.checkPackageName(key);
    }

    static isAppInstalledIOS(key) {
        return this.checkURLScheme(APP_LIST[key].urlScheme, APP_LIST[key].urlParams);
    }


    static openApp(key) {
        if (Platform.OS == 'ios') {
            Linking.openURL(key + '://').catch(err => console.log(err))
        } else {
            CheckPackageInstallation.openApp(key)
        }
    }

    static openAppInStore(link) {
        if (Platform.OS == 'ios') {
            let url = 'itms-apps://' + link;
            Linking
                .canOpenURL(url)
                .then((isInstalled) => {
                    if (isInstalled) {
                        Linking.openURL(url).catch(err => console.log(err))
                    }else
                    {
                        Linking.openURL('http://' + link).catch(err => console.log(err))
                    }
                })
                .catch((err) => {
                    Linking.openURL('http://' + link).catch(err => console.log(err))
                });
        } else {
            CheckPackageInstallation.openAppInStore(link)
        }
    }
}

export default AppInstalledChecker;
