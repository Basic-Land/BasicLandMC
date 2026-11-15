def runGradleTask(String task) {
	if (isUnix()) {
		sh 'chmod +x ./gradlew'
		sh "./gradlew ${task}"
	} else {
		bat ".\\gradlew.bat ${task}"
	}
}

pipeline {
	agent any

	options {
		timestamps()
	}

	stages {
		stage('applyAllPatches') {
			steps {
				script {
					runGradleTask('applyAllPatches')
				}
			}
		}

		stage('basiclandmc-server:createPaperclipJar') {
			steps {
				script {
					runGradleTask('basiclandmc-server:createMojmapPaperclipJar')
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

