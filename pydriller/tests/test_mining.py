import pytest
import sys
sys.path.insert(0, 'C:\Test\project-e13\pydriller\src')
from unittest.mock import Mock, patch
from mining import (calculate_commit_metrics, calculate_hunks_count,
                              process_repository, calculate_change_set_metrics,
                              calculate_code_churn_metrics, repo_export_to_csv,
                              commit_export_to_csv, main)

@pytest.fixture
def mock_commit():
    commit = Mock()
    commit.insertions = 10
    commit.deletions = 5
    commit.modified_files = [Mock(diff_parsed={'added': [], 'deleted': []})]
    commit.hash = 'abcd1234'
    commit.author.name = 'John Doe'
    commit.author.email = 'john@example.com'
    commit.author_date = '2023-01-01'
    commit.author_timezone = 'UTC'
    commit.in_main_branch = True
    commit.msg = 'Example commit message'
    return commit

@pytest.fixture
def mock_repository(mock_commit):
    repo = Mock()
    repo.traverse_commits.return_value = [mock_commit]
    return repo

def test_calculate_commit_metrics(mock_commit):
    mock_commit.insertions = 10
    mock_commit.deletions = 5
    lines_added, lines_removed, code_churn = calculate_commit_metrics(mock_commit)
    assert lines_added == 10
    assert lines_removed == 5
    assert code_churn == 5

def test_calculate_hunks_count(mock_commit):
    hunks_count = calculate_hunks_count(mock_commit)
    assert hunks_count == 0  # Modify this based on your actual logic

# @patch('mining.Repository', autospec=True)
# def test_process_repository(mock_repo):
#     mock_commit = Mock()
#     mock_repo.return_value.traverse_commits.return_value = [mock_commit]

#     result = process_repository('fake_repo_path')
#     assert isinstance(result, tuple) and len(result) == 8

def test_calculate_change_set_metrics():
    commit_count = 10
    change_sets = {1: 5, 2: 3, 3: 2}
    avg_change_set_size, max_change_set_size = calculate_change_set_metrics(commit_count, change_sets)
    assert avg_change_set_size == 1.7  # Modify this based on your actual logic
    assert max_change_set_size == 3

def test_calculate_code_churn_metrics():
    commit_count = 5
    total_code_churn = 20
    avg_code_churn = calculate_code_churn_metrics(commit_count, total_code_churn)
    assert avg_code_churn == 4.0  # Modify this based on your actual logic

def test_repo_export_to_csv(tmp_path):
    data = {'key': 'value'}
    csv_file_path = tmp_path / 'test_repo.csv'
    repo_export_to_csv(data, csv_file_path)
    assert csv_file_path.is_file()

def test_commit_export_to_csv(tmp_path):
    data = [{'key': 'value'}]
    csv_file_path = tmp_path / 'test_commit.csv'
    commit_export_to_csv(data, csv_file_path)
    assert csv_file_path.is_file()

# def test_main(monkeypatch, capsys):
#     monkeypatch.setattr('sys.argv', ['mining.py'])
#     with patch('mining.Repository', autospec=True) as mock_repo:
#         mock_commit = Mock()
#         mock_repo.return_value.traverse_commits.return_value = [mock_commit]

#         main()

#     captured = capsys.readouterr()
#     assert "Elapsed Time:" in captured.out