# ignore inline messages that are outside of the current diff
github.dismiss_out_of_range_messages

message "Thanks @#{github.pr_author} 👍👍"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("This pull request is a Work in Progress and not ready to merge") if github.pr_title.include? "[WIP]"

# Encourage contributors to write useful descriptions
warn("Please provide a Pull Request description ...") if github.pr_body.length < 20

# Notify of the release APK size.
apk_size = (File.size('app/build/outputs/apk/release/app-release-unsigned.apk').to_f / 2**20).round(2)
message "Release APK size: #{apk_size} MB"

# APK Analyzer
apkstats.command_type=:apk_analyzer
apkstats.apk_filepath='app/build/outputs/apk/release/app-release-unsigned.apk'
message(apkstats.file_size)
# message(apkstats.required_features)
# message(apkstats.non_required_features)
message(apkstats.permissions)
message(apkstats.min_sdk)
message(apkstats.target_sdk)
# message("#{apkstats.reference_count}")
# message("#{apkstats.dex_count}")

android_lint.report_file = "app/build/reports/lint-results-debug.xml"
android_lint.lint