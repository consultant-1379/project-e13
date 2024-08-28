import sys
import os
from pydriller import Repository
import pandas as pd
from collections import defaultdict
import time
from datetime import datetime, timezone, timedelta
import requests
url = "http://server:8080/data/"


def calculate_commit_metrics(commit):
    lines_added = commit.insertions
    lines_removed = commit.deletions
    code_churn = commit.insertions - commit.deletions

    return lines_added, lines_removed, code_churn

def calculate_hunks_count(commit):
    hunks_count = sum(len(modified_file.diff_parsed['added']) + len(modified_file.diff_parsed['deleted'])
                     for modified_file in commit.modified_files)
    return hunks_count

def process_repository(repo_path):
    commit_count = 0
    lines_added_total = 0
    lines_removed_total = 0
    code_churn_total = 0
    hunks_count_total = 0
    contributors = {}
    max_code_churn = -sys.maxsize - 1

    data_rows = []
    for commit in Repository(repo_path).traverse_commits():
        commit_count += 1

        lines_added, lines_removed, code_churn = calculate_commit_metrics(commit)
        lines_added_total += lines_added
        lines_removed_total += lines_removed
        code_churn_total += code_churn
        if int(code_churn) > int(max_code_churn):
            max_code_churn = code_churn

        name = commit.author.name
        if name in contributors:
            # Increment the value associated with the key by 1
            contributors[name] += 1
        else:
            # Add the key to the dictionary with an initial value of 1
            contributors[name] = 1

        tztime = process_time(commit.author_date,commit.author_timezone)
        timeinstring = tztime.strftime("%Y-%m-%d %H:%M:%S")
        hunks_count_total += calculate_hunks_count(commit)
        row_data = {
            #'url': repo_path,
            'commithash': str(commit.hash),
            'commitauthor': str(commit.author.name),
            #'commitauthoremail': commit.author.email,
            'commitauthordate': str(timeinstring),
            #'commitauthortimezone': commit.author_timezone,
            'commitlinesadded': str(commit.insertions),
            'commitlinesremoved': str(commit.deletions)
            #'commitinmain': commit.in_main_branch,
            #'commitmessage': commit.msg
        }
        data_rows.append(row_data)

    return commit_count, lines_added_total, lines_removed_total, code_churn_total, hunks_count_total, contributors, max_code_churn,data_rows


def process_time(time,timezone_off):
       # Create a timezone object using the provided offset
    commit_timezone = timezone(timedelta(seconds=timezone_off))

    # Convert to your local timezone
    local_commit_datetime = time.astimezone(commit_timezone)
    print(local_commit_datetime.strftime("%Y-%m-%d %H:%M:%S"))
    return local_commit_datetime
    
def calculate_change_set_metrics(commit_count, change_sets):
    avg_change_set_size = sum(size * count for size, count in change_sets.items()) / commit_count
    max_change_set_size = max(change_sets.keys())
    return avg_change_set_size, max_change_set_size

def calculate_code_churn_metrics(commit_count, total_code_churn):
    avg_code_churn = total_code_churn / commit_count
    return avg_code_churn

def repo_export_to_csv(data, csv_file_path):
    df = pd.DataFrame([data])
    df.to_csv(csv_file_path, index=False)
    print(f'Data has been exported to {csv_file_path}')
    
def commit_export_to_csv(data, csv_file_path):
    df = pd.DataFrame(data)
    df.to_csv(csv_file_path, index=False)
    print(f'Data has been exported to {csv_file_path}')

def main():
    start_time = time.time()
    repo_path = 'https://github.com/Vasu7389/react-project-ideas'

    commit_count, lines_added, lines_removed, code_churn, hunks_count, contributors, max_code_churn,commit_data = process_repository(repo_path)

    change_sets = defaultdict(int)
    for commit in Repository(repo_path).traverse_commits():
        change_sets[len(commit.modified_files)] += 1

    avg_change_set_size, max_change_set_size = calculate_change_set_metrics(commit_count, change_sets)
    print(code_churn)
    avg_code_churn = calculate_code_churn_metrics(commit_count, code_churn)

    big_cons = {}
    small_cons = {}
    for conkey,convalue in contributors.items():
        if(int(convalue) > (5 / 100) * commit_count):
            big_cons[conkey] = convalue
        else:
            small_cons[conkey] = convalue
    data = {
        'url': repo_path,
        'commits': commit_count,
        'linesadded': lines_added,
        'linesremoved': lines_removed,
        'avgchangeset': avg_change_set_size,
        'maxchangeset': max_change_set_size,
        'avgcodechurn': avg_code_churn,
        'maxcodechurn': max_code_churn,
        'contributorsnum': len(contributors),
        'bigcontributors': big_cons,
        'smallcontributors': small_cons,
        'hunkscount': hunks_count,
        'commitdata': commit_data
    }

    current_directory = os.getcwd()
    csv_filename = "repo.csv"
    csv_repo_path = os.path.join(current_directory, csv_filename)
    repo_export_to_csv(data, csv_repo_path)
    
    # csv_commit_path = "commit1.csv"
    


    # # Add more column names to the 'columns' list as needed
    # commit_export_to_csv(commit_data, csv_commit_path)
    
    with open(csv_repo_path, 'rb') as file:
    # Use the requests library to send a POST request with multipart/form-data
        response = requests.post(
            url,
            files={'file': (csv_repo_path, file, 'application/octet-stream')},
            headers={'accept': '*/*'},
    )
    
    print(response.status_code)
    os.remove(csv_repo_path)
    print('removed file')
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Elapsed Time: {elapsed_time} seconds")
    

if __name__ == "__main__":
    main()