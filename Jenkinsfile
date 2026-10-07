def runGradleTask(String task) {
	if (isUnix()) {
		sh 'chmod +x ./gradlew'
		sh "./gradlew ${task}"
	} else {
		bat ".\\gradlew.bat ${task}"
	}
}

def retryWithCleanWorkspace(int retries, Closure body) {
	retry(retries) {
		if (currentBuild.getPreviousBuild() != null) {
			cleanWs()
		}
		body()
	}
}

pipeline {
	agent any

	options {
		timestamps()
	}

	stages {
		stage('rebuildAllServerPatches') {
			steps {
				script {
					retryWithCleanWorkspace(2) {
						runGradleTask('rebuildAllServerPatches')
						runGradleTask('applyAllPatches')
					}
				}
			}
		}

		stage('basiclandmc-server:createPaperclipJar') {
			steps {
				script {
					runGradleTask('basiclandmc-server:createPaperclipJar')
				}
			}
		}

		stage('publishAllPublicationsToLocalRepository') {
			steps {
				script {
					runGradleTask('publishAllPublicationsToLocalRepository')
				}
			}
		}
	}

	post {
		always {
			archiveArtifacts artifacts: 'basiclandmc-server/build/libs/basiclandmc-paperclip-*.jar', fingerprint: true
		}
	}
}
