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
	    stage('rebuildAllServerPatches') {
			steps {
				script {
					runGradleTask('rebuildAllServerPatches')
					runGradleTask('applyAllPatches')
					runGradleTask('basiclandmc-server:createPaperclipJar')
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
