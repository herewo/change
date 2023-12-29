// https://wiki.in.ys4fun.com/pages/viewpage.action?pageId=92051660

def platform = 'android'

def label_name = 'JPUSa'

def workspace = "${project_path}/res_project"
def log_dir = "${project_path}/build_logs/CheckI18NAssets"
def unity_dir = 'C:\\Program Files\\Unity\\Hub\\Editor\\2021.3.30f1c1'
def unity = "\"${unity_dir}\\Editor\\Unity.exe\""


pipeline {
    agent {
        label {
            label "${label_name}"
            customWorkspace "${workspace}"
        }
    }
    stages {
        stage('1 - git更新') {
            steps { script{ dir("${workspace}") {
                echo '更新资源'
                build job: 'Template.Git.Win',
                parameters: [
                    string(name: 'label_name', value: "${label_name}"),
                    string(name: 'workspace', value: "${workspace}"),
                    string(name: 'branch', value: "${res_branch}"),
                    booleanParam(name: 'init_submodule', value: true),
                    booleanParam(name: 'git_clean', value: false),
                    booleanParam(name: 'git_sub_clean', value: true)
                ]
            }}}
        }
        stage('2 - 外服资源规范性检测') {
            steps { script{ dir("${workspace}") {
                bat "del /f /q errorResourcesInfo.txt"
                //运行检查I18资源方法
                try{
                    bat "${unity} -projectPath ${workspace}/client -logFile ${log_dir}/${env.BUILD_NUMBER}_build_I18NResCheck_jenkins.log -buildTarget ${platform} -executeMethod BuildResource.CheckI18NAssets -dst_dir ${workspace} -batchmode -nographics -quit"
                }
                catch(exc)
                {
                    unstable("检查出现错误！！！")                
                }
            }}}
        }
        stage('3 - 飞书通知') {
            steps { script{ dir("${workspace}") {
                bat "cd /d \"${project_path}\\send_msg\" && python FeishuRobot.py ${res_branch}"
            }}}
        }
    }
}
