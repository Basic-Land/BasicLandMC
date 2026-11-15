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
	    stage('build') {
			steps {
				script {
					runGradleTask('applyAllPatches')
					runGradleTask('basiclandmc-server:createPaperclipJar')
					runGradleTask('publishAllPublicationsToLocalRepository')
				}
			}
	    }
	}

	post {
		always {
			archiveArtifacts(
			    artifacts: '''
			        basiclandmc-server/build/libs/basiclandmc-paperclip-*.jar,
                    plugin/build/libs/asp-plugin-*.jar,
                    api/build/libs/api-*.jar
			    ''',
			    excludes: '**/*-sources.jar,**/*-javadoc.jar',
			    fingerprint: true
			)
		}
	}
}
