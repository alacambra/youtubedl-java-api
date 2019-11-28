def applicationName = "downloader";


pipeline{
    agent {
        label 'maven-j13'
    }

    stages{
        stage('build') {
            steps{
                sh script: "mvn clean test"
            }
        }

        stage('copy ui') {
            steps{
                sh script: "cp -Rf ui/src/* downloader-server/src/main/webapp/app"
            }
        }

         stage('package') {
            steps{
                sh script: "mvn package -Dmaven.test.skip=true"
            }
        }

        stage('s2i build'){
            steps{
                script{
                    openshift.withCluster(){
                        openshift.withProject(){
                            def build = openshift.selector("bc", applicationName);
                            def startedBuild = build.startBuild("--from-file=\"./downloader-server/target/downloader.war\"");
                            startedBuild.logs('-f');
                            echo "${applicationName} build status: ${startedBuild.object().status}";
                        }
                    }
                }
            }
        }
    }
}