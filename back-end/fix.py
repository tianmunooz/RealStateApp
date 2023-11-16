import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Initialize Firebase Admin
cred = credentials.Certificate('realstateapp-3c9b7-firebase-adminsdk-srrlk-53a71846c3.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

def convert_size_to_number():
    collection_name = 'real_estate_data_new'  # Replace with your Firestore collection name
    docs = db.collection(collection_name).stream()

    for doc in docs:
        doc_dict = doc.to_dict()
        size = doc_dict.get('size')

        if isinstance(size, str):
            try:
                # Convert the 'size' from string to integer
                size = size.replace('m', '').strip()  # Assuming the size ends with 'm'
                size = int(size) if size.isdigit() else None

                if size is not None:
                    # Update the document
                    db.collection(collection_name).document(doc.id).update({'size': size})
                    print(f"Updated document {doc.id} with new size: {size}")
            except ValueError as e:
                print(f"Error converting size for document {doc.id}: {e}")

convert_size_to_number()
